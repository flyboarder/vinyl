#!/bin/bash
set -o pipefail

if [ "$TRAVIS_BRANCH" = "master" ] && [ "$TRAVIS_PULL_REQUEST" = "false" ]
then
  git config --global user.email "samccone@gmail.com" && \
  git config --global user.name "auto deployer" && \

  deploy_ghpages () {
    echo "Deploying to GitHub Pages!" && \
    sed -i.tmp "s/\/\/ app.baseUrl = '\/polymer-starter-kit/app.baseUrl = '\/polymer-starter-kit/" app/scripts/app.js && \
    gulp build-deploy-gh-pages && \
    echo " Undoing Changes to PSK for GitHub Pages" && \
    cp app/scripts/app.js.tmp app/scripts/app.js && \
    rm app/scripts/app.js.tmp && \
    echo " DONE deploying to Github Pages!"
  }

  deploy_firebase () {
    echo "Deploying to Firebase! (https://polymer-starter-kit.firebaseapp.com)" && \
    echo " Making Changes to PSK for Firebase" && \
    sed -i.tmp 's/<!-- Chrome for Android theme color -->/<base href="\/">\'$'\n<!-- Chrome for Android theme color -->/g' app/index.html && \
    sed -i.tmp "s/hashbang: true/hashbang: false/" app/elements/routing.html && \
    cp app/docs/firebase.json app/firebase.json && \
    echo " Starting Build Process for Firebase Changes" && \
    gulp && \
    echo " Starting Deploy Process to Firebaseapp.com Server -- polymer-starter-kit.firebaseapp.com" && \
    firebase deploy --non-interactive --token "${FIREBASE_TOKEN}" && \
    echo " Undoing Changes to PSK for Firebase" && \
    cp app/index.html.tmp app/index.html && \
    cp app/elements/routing.html.tmp app/elements/routing.html && \
    rm app/elements/routing.html.tmp && \
    rm app/index.html.tmp && \
    rm app/firebase.json && \
    echo " DONE deploying to Firebase!"
  }

  deploy_ghpages && \
  deploy_firebase

else
  npm run lint
  npm test
fi
