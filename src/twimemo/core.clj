(ns twimemo.core
  (:use compojure.core)
  (:require [appengine-magic.core :as ae]))

(defroutes twimemo-app-handler
  (GET "/" [] "<h1>Hello World</h1>")
  (GET "/hello/:name" [name]
       {:status 200
        :headers {"Content-Type" "text/plain"}
        :body (format "Hello, %s!" name)})
  (ANY "*" _
       {:status 200
        :headers {"Content-Type" "text/plain"}
        :body "not found"}))

(ae/def-appengine-app twimemo-app #'twimemo-app-handler)
