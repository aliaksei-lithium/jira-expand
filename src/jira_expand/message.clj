(ns jira-expand.message)


(defn issues-from-text
  [text pattern]
  (set (map first (re-seq pattern text))))












