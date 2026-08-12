class Card 
{
    private String rank;
    private String suit;

    public Card(String rank, String suit) 
    {
        this.rank = rank;
        this.suit = suit;
    }

    public String toString() 
    {
        return rank + " of " + suit;
    }

    public boolean equals(Object obj) 
    {
        if (obj == null) {
            return false;
        }
        Card c = (Card) obj;
        if (rank.equals(c.rank) && suit.equals(c.suit)) {
            return true;
        } 
        else 
        {
            return false;
        }
    }

    public int hashCode() {
        return rank.length() + suit.length();
    }

    public static void main(String[] args) {
        Card[] cards = {
            new Card("Ace", "Spades"),
            new Card("King", "Hearts"),
            new Card("Queen", "Diamonds"),
            new Card("Ace", "Spades"),
            new Card("Jack", "Clubs")
        };

        boolean duplicateFound = false;
        for (int i = 0; i < cards.length; i++) 
        {
            for (int j = 0; j < i; j++) 
            {
                if (cards[i].equals(cards[j])) 
                {
                    System.out.println("Duplicate found: " + cards[i]);
                    duplicateFound = true;
                    break;
                }
            }
            if (duplicateFound) 
            {
                break;
            }
        }
    }
}
