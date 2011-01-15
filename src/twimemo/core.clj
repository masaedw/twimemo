(ns twimemo.core
  (:use compojure.core
        ring.util.response)
  (:require [appengine-magic.core :as ae]
            [appengine-magic.services.datastore :as ds])
  (:import java.util.Date
           java.util.UUID))

(ds/defentity Memo [^:key uuid content users])

(defn uuid []
  (str (UUID/randomUUID)))

(defn create-memo [req]
  "
<html>
  <head></head>
  <body>
    <form method='post'>
      <input type='text' name='memo' />
      <input type='submit' />
    </form>
  </body>
</html>")

(defn create-memo! [req]
  (let [content (get-in req [:params "memo"])
        x (Memo. (uuid) content nil)]
    (ds/save! x)
    (redirect "/memos")))

(defn link-to [url name]
  (format "<a href='%s'>%s</a>" url name))

(defn link-to-memo [memo]
  (link-to (format "/memos/%s" (:id memo))
           (format "memo-%s" (:id memo))))

(defn join [s coll]
  (pr-str (interpose s coll)))

(defn index-memo [req]
  (str
   "
<html>
  <head></head>
  <body>
    <h1>メモの一覧</h1>"
   (join "<br>" (map link-to-memo (ds/query :kind Memo)))
   "
  </body>
</html>"))

(defn show-memo [req]
  (let [id (get-in req [:params "id"])
        memo (ds/query :kind Memo :filter (= :id id))]
    (pr-str memo)
    ))

(defn hoge [req]
  )

(defroutes twimemo-app-handler
  (GET "/memos/create" req (create-memo req))
  (POST "/memos/create" req (create-memo! req))
  (GET "/memos" req (index-memo req))
  (GET "/memos/:id" req (show-memo req))
  (GET "/" req (index-memo req))
  (GET "/hoge" req
       (hoge req))
  (ANY "*" _
       {:status 404
        :headers {"Content-Type" "text/plain"}
        :body "not found"}))

(ae/def-appengine-app twimemo-app #'twimemo-app-handler)

#_(do
  (use :reload 'twimemo.core)
  (require '[appengine-magic.core :as ae])
  (ae/start twimemo-app :port 8080)
  (require '[appengine-magic.services.datastore :as ds])
  (import [com.google.appengine.tools.development.testing
           LocalServiceTestHelper
           LocalServiceTestConfig
           LocalDatastoreServiceTestConfig])
  (def helper (LocalServiceTestHelper. (into-array LocalServiceTestConfig [(LocalDatastoreServiceTestConfig.)])))
  (.setUp helper)
  )
