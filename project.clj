(defproject gol-cljs "0.1.0-SNAPSHOT"
  :description "Game of Life in the browser"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure       "1.7.0"]
                 [quil                      "2.3.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [prismatic/dommy           "1.1.0"]]

  :plugins [[lein-cljsbuild "1.1.1"]]
  :hooks   [leiningen.cljsbuild]

  :cljsbuild {:builds [{:source-paths ["src"]
                        :compiler     {:output-to     "js/main.js"
                                       :output-dir    "out"
                                       :main          "gol_cljs.core"
                                       :optimizations :none ;:advanced ;:none
                                       :pretty-print  true}}]})
