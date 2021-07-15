package com.miage.share.game.model.board;

import com.miage.share.game.model.Word;
import com.miage.share.game.viewmodel.ChoicePlayer;
import com.miage.share.share.Direction;

/**
 * The type Board.
 */
public class Board {

    private Case[][] cases;
    private int sizeMapX;
    private int sizeMapY;

    /**
     * Instantiates a new Board.
     */
    public Board() {
        this.cases = FactoryBoard.createCases();
        this.sizeMapX = this.cases.length;
        this.sizeMapY = this.cases[0].length;
    }

    /**
     * Get cases case [ ] [ ].
     *
     * @return the case [ ] [ ]
     */
    public Case[][] getCases() {
        return cases;
    }

    /**
     * Sets cases.
     *
     * @param cases the cases
     */
    public void setCases(Case[][] cases) { this.cases = cases; }

    /**
     * Get word position word.
     *
     * @param dirX   the dir x
     * @param dirY   the dir y
     * @param posX   the pos x
     * @param posY   the pos y
     * @param dirInc the dir inc
     * @return the word
     */
    public Word getWordPosition(int dirX, int dirY, int posX, int posY, int dirInc){
        int startX = posX;
        int startY = posY;

        int size = 0;
        int localX = startX + (dirX * dirInc);
        int localY = startY + (dirY * dirInc);
        StringBuilder builder = new StringBuilder();

        //while is not empty and not out of bound map save start position of word and inc size
        while(!this.isOutOfBound(localX,localY)){

            Case current = this.cases[localY][localX];

            if(current.isEmpty())
                break;

            builder.append(current.getValue());
            size++;
            localX = localX + (dirX * dirInc);
            localY = localY + (dirY * dirInc);

        }

        if(size==0) return null;

        String word = builder.toString();

        if(dirInc==1)
            return new Word(startX + (dirX * dirInc),startY + (dirY * dirInc),word.length(),dirX,dirY, word);
        else if(dirInc==-1){
            StringBuilder builderInv = new StringBuilder();
            for(int i = word.length()-1; i >= 0; i--){
                builderInv.append(word.charAt(i));
            }
            word = builderInv.toString();
            return new Word(startX-(dirX*word.length()),startY-(dirY*word.length()),word.length(),dirX,dirY, word);
        }

        return null;
    }


    /**
     * Get word with position word.
     *
     * @param dirX the dir x
     * @param dirY the dir y
     * @param posX the pos x
     * @param posY the pos y
     * @param ch   the ch
     * @return the word
     */
    public Word getWordWithPosition(int dirX, int dirY, int posX, int posY, char ch){

        Case current = this.cases[posY][posX];

        //size of word = nb loop-- and nb loop++
        int size = 1;
        //save start position of word
        int startX = posX;
        int startY = posY;
        //to iterate on loop
        int localX = posX-dirX;
        int localY = posY-dirY;

        //while is not empty and not out of bound map save start position of word and inc size
        while(!this.isOutOfBound(localX,localY)){

            current = this.cases[localY][localX];

            if(current.isEmpty())
                break;

            size++;
            startX = localX;
            startY = localY;
            localX = localX-dirX;
            localY = localY-dirY;
        }


        localX = posX+dirX;
        localY = posY+dirY;

        while(!this.isOutOfBound(localX,localY)){
            current = this.cases[localY][localX];

            if(current.isEmpty())
                break;

            size++;
            localX = localX+dirX;
            localY = localY+dirY;
        }

        //if size is equal to 1 is not a word
        if(size==1) return null;

        StringBuilder builder = new StringBuilder();
        int x = startX;
        int y = startY;
        for (int i = 0; i < size; i++ ){
            char value = this.cases[y][x].getValue();
            if(value==' '){
                value = ch;
            }
            builder.append(value);
            x=x+dirX;
            y=y+dirY;
        }

        return new Word(startX,startY,size,dirX,dirY, builder.toString());

    }

    /**
     * Is out of bound boolean.
     *
     * @param x the x
     * @param y the y
     * @return the boolean
     */
    public boolean isOutOfBound(int x, int y) {
        return x < 0 || x >= sizeMapX || y < 0 || y >= sizeMapY;
    }

    //Pas sur de laisser ça dans cette classe / A changer surement

    /**
     * Check empty boolean.
     *
     * @param posX the pos x
     * @param posY the pos y
     * @param dir  the dir
     * @return the boolean
     */
    public boolean checkEmpty(int posX, int posY, Direction... dir) {
    	for(Direction d : dir)  {
    		if(!this.isOutOfBound(posX+d.dirX,posY+d.dirY)&&!cases[posY+d.dirY][posX+d.dirX].isEmpty()) {
    			System.out.println("Direction is false : " + d);
    			return false;
    		}
		}
    	return true;
    }

    /**
     * Set word on board boolean.
     *
     * @param choice the choice
     * @return the boolean
     */
//   };
   //
   public boolean setWordOnBoard(ChoicePlayer choice){

       String word = choice.getWord();

       if(word == null || word.length()==0) return false;

       int size= word.length();
       int dirX = choice.getDirX();
       int dirY = choice.getDirY();
       int posX = choice.getStartX();
       int posY = choice.getStartY();

       if ((dirX != dirY) && (dirX==1 || dirX==0) && (dirY==1 || dirY==0) && !this.isOutOfBound(posX,posY)){

               for (int x = 0; x < size  ; x++) {
                   if(!this.cases[posY][posX].isEmpty()&&word.charAt(x)!=this.cases[posY][posX].getValue()){
                       System.out.println("bug"); //todo
                   }
                   this.cases[posY][posX].setValue(word.charAt(x));
                   posX=posX+dirX;
                   posY=posY+dirY;
               }
               return true;
       }
       return false;
   }

    /**
     * To string type string.
     *
     * @return the string
     */
    public String toStringType(){
        StringBuilder builder = new StringBuilder();
        for(int y = 0; y < this.cases.length; y++){
            for (int x = 0; x < this.cases[y].length; x++){
                builder.append(this.cases[y][x].getType().toString()).append("\t");
            }
            builder.append('\n');
        }
        return builder.toString();
    }

   @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for(int y = 0; y < this.cases.length; y++){
            for (int x = 0; x < this.cases[y].length; x++){
                char cha = this.cases[y][x].getValue();
                if(cha==' '){
                    cha='_';
                }
                builder.append(cha).append("\t");
            }
            builder.append('\n');
        }
        return builder.toString();
   }

    /**
     * Get cases dir case [ ].
     *
     * @param dirX  the dir x
     * @param dirY  the dir y
     * @param index the index
     * @return the case [ ]
     */
    public Case[] getCasesDir(int dirX, int dirY, int index) {

        if(dirX==1&&dirY==0) return this.cases[index];
        else if(dirX==0&&dirY==1){

            Case[] cases = new Case[15];
            for(int i = 0; i < 15; i++){
                cases[i] = this.cases[i][index];
            }
            return cases;
        }

        return null;
    }
}



