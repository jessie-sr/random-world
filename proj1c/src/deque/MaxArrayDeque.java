package deque;

import java.util.Comparator;

public class MaxArrayDeque<Et> extends ArrayDeque<Et> {

    private final Comparator<Et> defaultComparator;

    public MaxArrayDeque(Comparator<Et> c) {
        super();
        defaultComparator = c;

    }

    public Et max() {
        if (this.size() == 0) {
            return null;
        }
        Et max = this.get(0);
        for (int i = 1; i < size(); i++) {
            if (defaultComparator.compare(max, this.get(i)) < 0) {
                max = this.get(i);
            }
        }
        return max;
    }

    public Et max(Comparator<Et> otherComparator) {
        if (this.size() == 0) {
            return null;
        }
        Et max = this.get(0);
        for (int i = 1; i < size(); i++) {
            if (otherComparator.compare(max, this.get(i)) < 0) {
                max = this.get(i);
            }
        }
        return max;

    }

}
