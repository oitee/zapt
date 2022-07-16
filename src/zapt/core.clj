(ns zapt.core
  (:gen-class)
  (:require [taoensso.carmine :as car :refer (wcar)]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  "")

(def server-conn {:pool {}
                   :spec {:host "127.0.0.1" :port 9001}})

(defmacro wcar* [& body] `(car/wcar server-conn ~@body))
