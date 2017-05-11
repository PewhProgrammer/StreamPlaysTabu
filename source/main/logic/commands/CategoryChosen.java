package logic.commands;

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
        gameModel.generateExplainWord();
        gameModel.generateTabooWords();
    }

    @Override
    public boolean validate() {
        return true;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        CategoryChosen cg = (CategoryChosen) o;
        return category.equals(cg.getCategory());
    }
}
