package com.derek.assignment4;

    /*
     * class representing a listening
     * history of a particular artist (e.g.
     * UserListen(5, 20) indicates artist 5
     * has been listened to 20 times
     */
    class Listen implements Comparable<Listen> {
        int id;
        int count;
        String name;

        public Listen(int id, int count, String name) {
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
        public int compareTo(Listen o) {
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
            return (o instanceof Listen) && ((Listen) o).getId() == id;
        }
    }