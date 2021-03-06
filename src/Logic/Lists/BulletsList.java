package Logic.Lists;

import Game.Attack;

public class BulletsList {

    public BulletsNodes head;
    private int large;
    private static BulletsList list = new BulletsList();

    public static BulletsList getInstance(){
        return list;
    }

    public static void setInstance(BulletsList list1){
        list = list1;
    }

    public static void reset(){
        list = new BulletsList();
    }

    /**
     * Constructor of the BulletsList class.
     */
    private BulletsList(){
        this.head = null;
        this.large = 0;
    }

    public int getLarge() {
        return large;
    }

    public void setLarge(int large) {
        this.large = large;
    }

    /**
     * Method to add a bullet to the list.
     * @param bullet instance of the attack of the dragon or the gryphon.
     */
    public void addBullet(Attack bullet){
        if (this.head == null){
            this.head = new BulletsNodes(bullet);
            this.large++;
        }else{
            BulletsNodes tmp = this.head;
            while (tmp.next != null){
                tmp = tmp.next;
            }
            tmp.next = new BulletsNodes(bullet);
            this.large++;
        }
    }

    /**
     * Method to delete a specific bullet of the list.
     * @param bullet instance of the attack to delete.
     */
    public void deleteBullet(Attack bullet){
        if (this.head.getAttack() == bullet){
            this.head = this.head.next;
            this.large--;
        }else{
            BulletsNodes tmp = this.head;
            while (tmp.next != null){
                if (tmp.next.getAttack() == bullet && tmp.next.next == null){
                    tmp.next = null;
                    this.large--;
                    break;
                }else if(tmp.next.getAttack() == bullet){
                    tmp.next = tmp.next.next;
                    this.large--;
                    break;
                }else{
                    tmp = tmp.next;
                }
            }
        }
    }

}
