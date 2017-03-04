(ns jira-expand.web
  (:require
    [cheshire.core :as json]
    [aleph.http :as aleph]))

(def slack-api-base-url "https://slack.com/api")
(def api-token "xoxp-3612762001-42353140468-123718175202-cc8b6429bc0247e8265f61333607be5e")

(defn- get-api-response
  "Takes a full http response map and returns the api response as a map."
  [http-response]
  (let [response-body-bytes (:body http-response)
        response-body-json (byte-streams/to-string response-body-bytes)
        api-response (json/parse-string response-body-json true)]
    api-response))

(defn- call-slack-web-api
  ([method-name]
   (call-slack-web-api method-name {}))
  ([method-name params]
   (let [method-url-base (str slack-api-base-url "/" method-name)]
     @(aleph/post method-url-base {:query-params params}))))

(defn rtm-start
  [api-token]
  (->> {:token api-token}
       (call-slack-web-api "rtm.start")
       (get-api-response)))


(defn channels-list
  ([api-token]
   (channels-list api-token false))
  ([api-token exclude-archived]
   (->> {:token api-token :exclude_archived (if exclude-archived 1 0)}
        (call-slack-web-api "channels.list")
        (get-api-response)
        :channels)))