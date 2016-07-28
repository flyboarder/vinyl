(set-env!
 :dependencies  '[[org.clojure/clojure       "1.7.0"]
                  [org.clojure/clojurescript "1.7.170"]
                  [adzerk/boot-cljs          "1.7.170-3"]
                  [compojure                 "1.4.0"]
                  [ring                      "1.4.0"]
                  [ring/ring-defaults        "0.1.5"]
                  [hiccup                    "1.0.5"]
                  [pandeiro/boot-http        "0.7.0"]
                  [hoplon/boot-hoplon        "0.1.13"]
                  [hoplon/hoplon             "6.0.0-alpha11"]
                  [hoplon/javelin            "3.8.4"]
                  [hoplon/castra             "3.0.0-alpha1"]
                  [degree9/silicone          "0.6.0-SNAPSHOT"]
                  [degree9/boot-polymer      "0.2.0-SNAPSHOT"]
                  [degree9/boot-bower        "0.5.0-SNAPSHOT"]
                  [degree9/boot-semver       "1.2.0"  :scope "test"]]
 :resource-paths #{"src" "resources/assets"})

(require
 '[adzerk.boot-cljs   :refer :all]
 '[hoplon.boot-hoplon :refer :all]
 '[pandeiro.boot-http :refer :all]
 '[boot-semver.core   :refer :all]
 '[degree9.boot-exec :refer :all]
 '[degree9.boot-bower :refer :all]
 '[degree9.boot-polymer :refer :all])

(task-options!
 pom {:project 'degree9/vinyl
      :description ""
      :url         ""
      :scm {:url ""}})

(deftask bower-deps
  "Fetch bower deps."
  []
  (bower :install {:iron-elements  "PolymerElements/iron-elements#master"
                   :paper-elements "PolymerElements/paper-elements#master"
                   :neon-elements  "PolymerElements/neon-elements#master"
                   }))

(deftask build
  "Compile sources"
  []
  (comp
   (hoplon :pretty-print  true)
   (cljs   :optimizations :none
           :source-map    true)
   (target :dir #{"target"})))

(deftask dev
  "Build vinyl for local development."
  []
  (comp
   (version)
   (bower-deps)
   (watch)
   (polymer)
   ;(show :fileset true)
   (build)
   (serve :reload true :port 8080)))
