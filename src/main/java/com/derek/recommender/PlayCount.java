package com.derek.recommender;

    /*
     * class representing a listening
     * history of a particular artist (e.g.
     * PlayCount(5, 20) indicates artist 5
     * has been listened to 20 times
     */
    class PlayCount implements Comparable<PlayCount> {
        int id;
        int count;
        String name;

        public PlayCount(int id, int count, String name) {
            this.id = id;
            this.count = count;
            this.name = name;
        }

        public int getId() {
            return id;
        }
        public int getCount() {
            return count;
        }

        @Override
        public String toString() {
            return name + ", Play Count: " + count;
        }

        @Override
        public int compareTo(PlayCount o) {
            if(count < o.getCount()) {
                return -1;
            } else if(count > o.getCount()) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public boolean equals(Object o) {
            return (o instanceof PlayCount) && ((PlayCount) o).getId() == id;
        }
    }