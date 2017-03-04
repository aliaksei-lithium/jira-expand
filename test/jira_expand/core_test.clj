(ns jira-expand.core-test
  (:require [clojure.test :refer :all]
            [jira-expand.core :refer :all]
            [jira-expand.message :refer :all]
            [jira-expand.web :refer :all]
            [cheshire.core :as json]
            [aleph.http :as aleph]
            ))

(deftest a-test
  (testing "Extract issues from text"
    (let [msg "Hello guys! Please check ST-455 and CCO-123  CCO-123 CCO-123 CCO-125AND FIX IT!!!111"]
      (is (= (issues-from-text msg #"(ST|CCO)-\d+") #{"CCO-123" "ST-455" "CCO-125"})))
    ))

(deftest common-tests
  (testing "json"
    (println (json/generate-string {:foo "bar" :baz {:eggplant [1 2 3]}} {:pretty true}))
    )
  ;(testing "http-sync"
  ;  (-> @(aleph/get "https://google.com")
  ;      :body
  ;      (byte-streams/to-string)
  ;      (println)
  ;      )
  ;  )
  (testing "slack-api-test"
    (println (channels-list api-token))
    ))

