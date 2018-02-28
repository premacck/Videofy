package com.cncoding.teazer.model.post;

import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.utilities.Annotations.CallType;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class PostReactionsList extends BaseModel {
    private boolean next_page;
    private ArrayList<PostReaction> reactions;

    public PostReactionsList(boolean next_page, ArrayList<PostReaction> reactions) {
        this.next_page = next_page;
        this.reactions = reactions;
    }

    public PostReactionsList(Throwable error) {
        this.error = error;
    }

    public PostReactionsList setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<PostReaction> getReactions() {
        return reactions;
    }
}