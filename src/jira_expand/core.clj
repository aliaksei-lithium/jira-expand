(ns jira-expand.core
  (:gen-class)
  (:require
    [jira-expand
     [team-state :as state]
     [web :as web]
     [connection :as conn]
     [rtm-receive :as rx]]
     [clj-time.coerce :refer [to-long]]
    [manifold.stream :as stream]))



(defn disconnect
  []
  (conn/disconnect))


(defn connect
  ([api-token host-handle-event]
   (connect api-token host-handle-event {:log true}))
  ([api-token host-handle-event options]
   (let [rx-event-stream (stream/stream 8)
         pass-event-to-rx #(stream/put! rx-event-stream %)
         host-event-stream (stream/stream 8)
         pass-event-to-host #(stream/put! host-event-stream %)
         reconnect (fn [] (do (when (options :log)
                                (println "reconnecting..."))
                              (disconnect)
                              (connect api-token host-handle-event options)))]
     (stream/consume #(rx/handle-event % pass-event-to-host) rx-event-stream)
     (stream/consume host-handle-event host-event-stream)
     (conn/start-real-time api-token state/set-team-state pass-event-to-rx reconnect options))))

;
;(defn time->ts
;  "converts a joda DateTime into an approximate slack message timestamp"
;  [time]
;  (-> time
;      (to-long)
;      (/ 1000)
;      (int)
;      (str)))



(defn printex
  [msg ex]
  (let [line (apply str (repeat 100 "-"))]
    (println line)
    (println msg)
    (println ex)
    (clojure.stacktrace/print-stack-trace ex)
    (println line)))

(defn try-handle-slack-event
  [event]
  (try
    (println event)
    (catch Exception ex
      (printex (str "Exception trying to handle slack event\n" (str event) ".") ex))))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (connect web/api-token try-handle-slack-event))

(def display print)
(display "Hello world!")