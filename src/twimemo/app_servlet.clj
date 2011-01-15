(ns twimemo.app_servlet
  (:gen-class :extends javax.servlet.http.HttpServlet)
  (:use twimemo.core)
  (:use [appengine-magic.servlet :only [make-servlet-service-method]]))


(defn -service [this request response]
  ((make-servlet-service-method twimemo-app) this request response))
