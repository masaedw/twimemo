(ns twimemo.core
  (:use compojure.core
        ring.util.response)
  (:require [appengine-magic.core :as ae]
            [appengine-magic.services.datastore :as ds])
  (:import java.util.Date))

(ds/defentity Memo [^:key content users])
(ds/defentity Author [^:key name birthday])

(defroutes twimemo-app-handler
  (GET "/memos/create" []
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
  (POST "/memos/create" request
        (let [content (get-in request [:params "memo"])
              x (Author. content nil)]
          (ds/save! x)
          (redirect "/memos")))
  (GET "/memos" []
       (str
"
<html>
  <head></head>
  <body>
    <h1>memo list</h1>"
(vec (map (fn [x] (.name x)) (ds/query :kind twimemo.core.Author)))
"
  </body>
</html>"))
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

;; (use :reload 'twimemo.core)
;; (require '[appengine-magic.core :as ae])
;; (ae/start twimemo-app :port 8080)
;; (require '[appengine-magic.services.datastore :as ds])
