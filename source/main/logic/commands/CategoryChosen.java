package logic.commands;

import logic.bots.SiteBot;
import model.GameModel;

/**
 * Created by Marc on 05.04.2017.
 */
public class CategoryChosen extends Command {

    private String category;

    public CategoryChosen(GameModel gm, String ch, String category) {
        super(gm, ch);
        this.category = category;

    }

    /**
     * Updates the current category to the giver's choice
     */
    @Override
    public void execute() {
        gameModel.setCategory(category);
        gameModel.getSiteBot().sendWord(gameModel.getExplainWord(), gameModel.getTabooWords());
    }

    @Override
    public boolean validate() {
        //TODO anything to do here?
        return true;
    }
}
