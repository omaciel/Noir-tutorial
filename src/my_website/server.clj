(ns my-website.server
  (:require [noir.server :as server])
  (:gen-class))

(server/load-views "src/my_website/views/")

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (server/start port {:mode mode
                        :ns 'my-website})))
