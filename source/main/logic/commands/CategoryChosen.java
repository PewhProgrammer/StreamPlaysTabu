package logic.commands;

import logic.bots.SiteBot;
import model.GameModel;

/**
 * Created by Marc on 05.04.2017.
 */
public class CategoryChosen extends Command {

    private String category;
    private SiteBot siteBot;

    public CategoryChosen(GameModel gm, String ch, String category, SiteBot siteBot) {
        super(gm, ch);
        this.category = category;
        this.siteBot = siteBot;

    }

    /**
     * Updates the current category to the giver's choice
     */
    @Override
    public void execute() {
        this.gameModel.setCategory(category);
        //TODO send word2explain + taboowords
        //update model, inform observer
    }

    @Override
    public boolean validate() {
        //TODO anything to do here?
        return false;
    }
}
