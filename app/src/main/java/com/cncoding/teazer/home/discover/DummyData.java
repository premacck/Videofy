package com.cncoding.teazer.home.discover;

import com.cncoding.teazer.utilities.Pojos;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * Created by Prem $ on 11/23/2017.
 */

public class DummyData {

    private static String[] thumbUrls = new String[] {
            "https://image.freepik.com/free-vector/white-party-background-with-dancing-silhouettes_1048-932.jpg",
            "https://i.ytimg.com/vi/t6PmB6tMBOc/hqdefault.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/9/96/NWFusion7.jpg/310px-NWFusion7.jpg",
            "https://i.pinimg.com/736x/1d/9e/07/1d9e07f0f0f8d3fa8f250bc91a3a5ab1.jpg",
            "https://melbourne.lanewaylearning.com/wp-content/uploads/sites/7/2015/03/Two_dancers.jpg",
            "http://www.loisboothdancing.co.uk/wp-content/uploads/2016/02/lose-yourself-in-dance-picture.png",
            "https://i.pinimg.com/736x/1d/9e/07/1d9e07f0f0f8d3fa8f250bc91a3a5ab1.jpg",
            "http://media.gettyimages.com/photos/young-woman-hula-dancer-in-traditional-clothing-dancing-on-black-picture-id120989399",
            "http://donthatethegeek.com/wp-content/uploads/2014/09/funny-dog-sandwich-burger-cheese.jpg",
            "https://media.giphy.com/media/HWwq0LGAP8u0o/giphy.gif",
            "http://www.bajiroo.com/wp-content/uploads/2014/03/lets-play-little-elephant-funny-cute-animals-pics.jpg",
            "https://am22.akamaized.net/tms/cnt/uploads/2012/01/hippophin.jpg",
            "https://www.miataturbo.net/attachments/insert-bs-here-4/180382d1499404602-random-pictures-thread-only-rule-post-here" +
                    "-more-entertain-me-randomfloatingpuffballcats.gif",
            "http://www.vitamin-ha.com/wp-content/uploads/2014/04/VH-Random-duckhourse.jpg",
            "http://uploads.neatorama.com/images/posts/522/66/66522/1383143078-0.jpg",
            "http://cdn-news.wgbh.org/s3fs-public/styles/post_16x9/public/trump-072015.jpg",
            "http://www.fullredneck.com/wp-content/uploads/2016/03/Funny-Donald-Trump-Jokes.jpg",
            "http://www.mulierchile.com/random-image/random-image-001.jpg",
            "https://vignette.wikia.nocookie.net/simpsons/images/f/f2/423.jpg",
            "http://www.tridanim.com/images/images4.fanpop.com/image/photos/23300000/Er-weird-O-o-random-23398022-600-678.jpg",
            "https://i.imgur.com/MrDX1w3.gif",
            "https://us.123rf.com/450wm/Prometeus/Prometeus1512/Prometeus151200454/49388466-black-and-white-portrait-of-a-" +
                    "beautiful-young-couple-dancing-tango-with-passion-professional-dancers.jpg?ver=6",
            "https://i.pinimg.com/736x/57/bc/9a/57bc9afb31de85ddc1214d768e21a513--dance-humor-portrait.jpg",
            "https://photo.johanneshjorth.se/wp-content/uploads/2017/07/hjorthmedh-casual-dancing-58.jpg",
            "http://www.qygjxz.com/data/out/193/5488513-random-picture.gif"
    };
    private static String[] dps = new String[] {
            "https://usercontent2.hubstatic.com/12599759_100.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_100_100/AAEAAQAAAAAAAAzdAAAAJDIxNmE1ZGRiLWI1YzAtNDUwZC1iZjZhLWNkNjI0ZTYyYjA0Ng.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_100_100/AAEAAQAAAAAAAAwtAAAAJDdjMjIwNDdhLTc0YjItNGQ0NC05YzU0LTlhYmY2NjhmZTY4Zg.jpg",
            "http://forums.joinsquad.com/uploads/profile/photo-thumb-214.png",
            "http://cdn-frm-eu.wargaming.net/wot/eu//profile/photo-thumb-4321827.png",
            "https://static.bikehub.co.za/uploads/profile/photo-thumb-3340.jpg",
            "http://cdn-frm-us.wargaming.net/wot/us//profile/photo-thumb-1880197.png",
            "https://media.licdn.com/mpr/mpr/shrink_100_100/p/2/000/052/04c/3da6e3e.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_100_100/AAEAAQAAAAAAAAc6AAAAJDhiNGMyOWFjLWQ1MjAtNDE2OC05N2JlLTAzZGFiMTk0ODM4Yw.jpg",
            "https://mlpforums.com/uploads/profile/photo-thumb-4945.jpg",
            "https://images.8tracks.com/avatar/i/008/907/732/Monkey-D-8896.jpg",
            "https://forum-new.worldofwarships.eu/uploads/profile/96/32/87/photo-thumb-500873296-55604b15.jpg"
    };
    private static String[] titles = new String[] {
            "Last night bash at Delhi Fort",
            "That couldn't be better!",
            "Wish you were there",
            "Can someone please tell me what's going on?",
            "You won't get away with this",
            "So funny :D",
            "LMAO!!!",
            "HaHaHaHaHaHaHaHa!",
            "Whoa!",
            "That was nuts bro!",
            "When you see it...",
            "Don’t take life too seriously; No one gets out alive.",
            "You’re just jealous because the voices only talk to me.",
            "Beauty is in the eye of the beer holder.",
            "Earth is the insane asylum for the universe.",
            "I’m not a complete idiot — Some parts are missing.",
            "Out of my mind. Back in five minutes.",
            "God must love stupid people; He made so many.",
            "The gene pool could use a little chlorine.",
            "Consciousness: That annoying time between naps.",
            "Ever stop to think, and forget to start again?",
            "I Have a Degree in Liberal Arts; Do You Want Fries With That?",
            "A hangover is the wrath of grapes.",
            "Stupidity is not a handicap. Park elsewhere!",
            "They call it PMS because Mad Cow Disease was already taken.",
            "He who dies with the most toys is nonetheless dead.",
            "A picture is worth a thousand words, but it uses up three thousand times the memory.",
            "Ham and eggs. A day’s work for a chicken, a lifetime commitment for a pig.",
            "The trouble with life is there’s no background music.",
            "The original point and click interface was a Smith and Wesson."
    };
    private static String[] names = new String[] {
            "Waylon Dalton",
            "Justine Henderson",
            "Abdullah Lang",
            "Marcus Cruz",
            "Thalia Cobb",
            "Mathias Little",
            "Eddie Randolph",
            "Angela Walker",
            "Lia Shelton",
            "Hadassah Hartman",
            "Joanna Shaffer",
            "Jonathon Sheppard",
            "Joane Bayley",
            "Blanch Ardis",
            "Pasquale Andresen",
            "Loise Lebel",
            "Carlton Obrien",
            "Reinaldo Briscoe",
            "Teresa Sowards",
            "Kendra Camacho",
            "Youlanda Desmarais",
            "Ella Stutz",
            "Lourdes Brott",
            "Branden Hisle",
            "Royce Goudy",
            "Patience Riddle",
            "Albertha Sandefur",
            "Kayleen Shanks",
            "Dacia Jacobsen",
            "Lynna Mccane",
            "Isaac Brandenberger",
            "Jeremiah Old"
    };

    static String getRandomThumbUrl() {
        return thumbUrls[new Random().nextInt(thumbUrls.length - 1)];
    }

    static String getRandomDp() {
        return dps[new Random().nextInt(dps.length - 1)];
    }

    static String getRandomName() {
        return names[new Random().nextInt(names.length - 1)];
    }

    static String getRandomTitle() {
        return titles[new Random().nextInt(titles.length - 1)];
    }

    static int getRandomDuration() {
        return new Random().nextInt(60);
    }

    static int getRandomNumber() {
        return new Random().nextInt(500);
    }

    private static Pojos.DummyDiscover.DummyMostPopular getMostPopular() {
        return new Pojos.DummyDiscover.DummyMostPopular(getRandomTitle(), getRandomDuration(), getRandomThumbUrl(), getRandomDp(),
                getRandomName(), getRandomNumber(), getRandomNumber(), getRandomDp(), getRandomDp(), getRandomDp(), getRandomNumber());
    }

    private static Pojos.DummyDiscover.MyDummyInterests getMyInterests() {
        return new Pojos.DummyDiscover.MyDummyInterests(getRandomTitle(), getRandomThumbUrl(), getRandomDp(), getRandomName(),
                getRandomNumber(), getRandomNumber(), getRandomDp(), getRandomDp(), getRandomDp(), getRandomNumber());
    }

    private static Pojos.DummyDiscover.DummyFeaturedVideos getFeaturedVideos() {
        return new Pojos.DummyDiscover.DummyFeaturedVideos(getRandomTitle(), getRandomThumbUrl(),
                getRandomDp(), getRandomName(), getRandomNumber(), getRandomNumber());
    }

    static ArrayList<Pojos.DummyDiscover.DummyMostPopular> getMostPopularList() {
        ArrayList<Pojos.DummyDiscover.DummyMostPopular> mostPopularArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mostPopularArrayList.add(getMostPopular());
        }
        return mostPopularArrayList;
    }

    static ArrayList<Pojos.DummyDiscover.MyDummyInterests> getMyInterestsList() {
        ArrayList<Pojos.DummyDiscover.MyDummyInterests> myInterestsArrayList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            myInterestsArrayList.add(getMyInterests());
        }
        return myInterestsArrayList;
    }

    static ArrayList<Pojos.DummyDiscover.DummyFeaturedVideos> getFeaturedVideosList() {
        ArrayList<Pojos.DummyDiscover.DummyFeaturedVideos> featuredVideosArrayList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            featuredVideosArrayList.add(getFeaturedVideos());
        }
        return featuredVideosArrayList;
    }
}