package com.derek.assignment4;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.MaxPQ;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Recommender {
    private Graph users; // graph in which vertices represent users, and edges represent friendships between users
    private Map<Integer, Artist> artistMap = new HashMap<>(); // map between each artist ID and the corresponding artist object
    private Map<Integer, HashSet<Listen>> listenMap = new HashMap<>(); // map from each user id to a set of UserListen objects, representing how much they listen to each artist

    public static void main(String[] args) {
        Recommender r = new Recommender();
        r.listTop10();
    }

    /*
     * nested class representing a listening
     * history of a particular artist (e.g.
     * UserListen(5, 20) indicates artist 5
     * has been listened to 20 times
     */
    class Listen implements Comparable<Listen> {
        int id;
        int count;

        public Listen(int id, int count) {
            this.id = id;
            this.count = count;
        }

        public int getId() {
            return id;
        }
        public int getCount() {
            return count;
        }

        @Override
        public String toString() {
            return "Artist: " + id + " Count: " + count;
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

    public Recommender() {
        // load user friend data
        try {
            users = loadUsers("/Users/DerekAvery/Desktop/music-recommender/src/main/java/com/derek/assignment4/dataset/user_friends.dat");
        } catch(FileNotFoundException fnfe) {
            System.out.println("Could not open user-friend file.");
            fnfe.printStackTrace();
        }

        // load artist data
        try {
            loadArtists("/Users/DerekAvery/Desktop/music-recommender/src/main/java/com/derek/assignment4/dataset/artists.dat");
        } catch(FileNotFoundException fnfe) {
            System.out.println("Could not open artist file.");
            fnfe.printStackTrace();
        }

        // load user listening info
        try {
            loadListenInfo("/Users/DerekAvery/Desktop/music-recommender/src/main/java/com/derek/assignment4/dataset/user_artists.dat");
        } catch(FileNotFoundException fnfe) {
            System.out.println("Could not open user-artist file.");
            fnfe.printStackTrace();
        }
    }

    /*
     * creates a graph from a line-separated file of user friendships
     * (two whitespace-separated integers per line represent these relationships)
     * in which vertex indices represent user id, and
     * edges between vertices represent friendships between two users
     * with the id's corresponding to each vertex of the edge
     */
    public Graph loadUsers(String fileName) throws FileNotFoundException {
        int maxId = 0; // maximum value of a user id (used to determine size of the user graph)
        Scanner in = new Scanner(new File(fileName)); // Scanner to parse user data file
        ArrayList<Integer> user1 = new ArrayList<>(); // first user in user-to-user connection
        ArrayList<Integer> user2 = new ArrayList<>(); // second user in user-to-user connection
        int line = 0; // current line in file being parsed

        // parse file and add the id of each user-to-user connection
        // to two separate array lists at the same index
        while(in.hasNextLine()) {
            line++;
            String[] curLine = in.nextLine().split("\t");
            if(curLine.length > 1) {
                try { // add user id pairs to separate lists; add unique users to 'users' set
                    int id1 = Integer.parseInt(curLine[0]);
                    int id2 = Integer.parseInt(curLine[1]);
                    if(id1 > maxId) {
                        maxId = id1;
                    } else if(id2 > maxId) {
                        maxId = id2;
                    }
                    user1.add(id1);
                    user2.add(id2);
                } catch(NumberFormatException nfe) {
                    System.out.println("Discarded the line with content: [" + line + "] - content improperly formatted");
                }
            }
        }

        // create new user graph and add edges between connected users;
        // increase graph size by 1 since '0' is not used as a user ID
        Graph graph = new Graph(maxId+1);
        for(int i = 0; i < user1.size(); i++) {
            graph.addEdge(user1.get(i), user2.get(i));
        }
        return graph;
    }

    /*
     * extracts information about all artists in a tab-separated data file
     * to populate a HashMap mapping from each artist id to a new
     * artist object which includes their id, name, URL and pictureURL
     */
    public void loadArtists(String fileName) throws FileNotFoundException {
        Scanner in = new Scanner(new File(fileName)); // Scanner to parse artist data file
        while(in.hasNextLine()) {
            String line = in.nextLine();
            String[] fields = line.split("\t");
            try {
                int id = Integer.parseInt(fields[0]);
                String name = fields[1];
                if(!artistMap.containsKey(id)) {
                    artistMap.put(id, new Artist(id, name));
                }
            } catch(Exception e) {
                System.out.println("Discarded the line with content: [" + line + "] - content improperly formatted");
            }
        }
    }

    /*
     * fills the userArtists map with mappings between each user
     * in the data file, and new sets of artists they listen to
     * (the Artist elements in each set will contain a weight
     * corresponding to the number of times the user is specified
     * to have listened to them, based on the 3rd
     */
    public void loadListenInfo(String fileName) throws FileNotFoundException {
        Scanner in = new Scanner(new File(fileName)); // Scanner to parse artist data file
        while(in.hasNextLine()) {
            String line = in.nextLine();
            String[] fields = line.split("\t");
            try {
                int user = Integer.parseInt(fields[0]);
                int artist = Integer.parseInt(fields[1]);
                int listens = Integer.parseInt(fields[2]);
                if(!listenMap.containsKey(user)) {
                    listenMap.put(user, new HashSet<Listen>());
                }
                listenMap.get(user).add(new Listen(artist, listens));
            } catch(Exception e) {
                System.out.println("Discarded the line with content: [" + line + "] - content improperly formatted");
            }
        }
    }

    /*
     * print top 10 overall artists
     */
    public void listTop10() {
        for(Integer artist : top10(listenMap.keySet())) {
            System.out.println(artistMap.get(artist));
        }
    }

    /*
     * print top 10 artists among a user
     * @param user and his/her friends
     */
    public void recommend10(int user) {
        Set<Integer> users = friends(user);
        users.add(user);
        for(Integer artist : top10(users)) {
            System.out.println(artistMap.get(artist));
        }
    }

    /*
     * returns the set of artists with the most music
     * plays by users in the set @param users
     */
    public Set<Integer> top10(Set<Integer> users) {
        Map<Integer, Listen> map = new HashMap<>(); // map from each artist to their total play count

        // create map from each artist id to their total song play count among users in the set
        for(Integer user : users) {
            for(Listen listen : listenMap.get(user)) {
                if(map.containsKey(listen.getId())) { // increment count of existing artists' 'Listen' values
                    Listen cur = map.get(listen.getId());
                    map.put(cur.getId(),
                            new Listen(cur.getId(), map.get(cur.getId()).getCount() + cur.getCount()));
                } else {
                    map.put(listen.getId(), listen);
                }
            }
        }
        MaxPQ<Listen> pQueue = new MaxPQ<>();
        Set<Integer> top10 = new HashSet<>();

        // add all 'Listen' objects to priority queue
        for(Integer listen : map.keySet()) {
            pQueue.insert(map.get(listen));
        }

        // remove top 10 Listen objects from priority queue, and add them to the result set
        for(int i = 0; i < 10; i++) {
            top10.add(pQueue.delMax().getId());
        }
        return top10;
    }

    /*
     * prints ID's of all friends of @param user
     * to std. out
     */
    public void listFriends(int user) {
        System.out.print("Friends of user " + user + ": ");
        for(Integer friend : friends(user)) {
            System.out.print(friend + " ");
        }
        System.out.println();
    }

    /*
     * prints ID's of all friends common between
     * @param user1 and @param user2
     */
    public void commonFriends(int user1, int user2) {
        System.out.print("Friends of users " + user1 + " and " + user2 + ": ");
        for(Integer friend : common(user1, user2)) {
            System.out.print(friend + " ");
        }
        System.out.println();
    }

    /*
     * prints all artists' names (or id's if names are not found)
     * of artists listened to by both @param user1 and @param user2
     */
    public void listArtists(int user1, int user2) {
        for(Integer artist : artists(user1, user2)) {
            if(artistMap.containsKey(artist)) {
                System.out.println(artistMap.get(artist).getName());
            } else {
                System.out.println("Artist ID: " + artist);
            }
        }

    }

    /*
     * returns the set of artist id's of artists
     * listened to by both @param user1 and @param user2
     */
    private Set<Integer> artists(int user1, int user2) {
        Set<Integer> artists1 = new HashSet<>();
        Set<Integer> artists2 = new HashSet<>();
        for(Listen listen : listenMap.get(user1)) {
            artists1.add(listen.getId());
        }
        for(Listen listen : listenMap.get(user2)) {
            artists2.add(listen.getId());
        }

        artists1.retainAll(artists2);
        return artists1;
    }

    /*
     * returns a set of id's of
     * all the friends of @param user
     */
    private Set<Integer> friends(int user) {
        Set<Integer> friends = new HashSet<>();
        for(Integer friend : users.adj(user)) {
            friends.add(friend);
        }
        return friends;
    }

    /*
     * returns a set of all friends shared
     * between @param user1 and @param user2
     */
    private Set<Integer> common(int user1, int user2) {
        Set<Integer> friends = friends(user1);
        friends.retainAll(friends(user2));
        return friends;
    }
}
