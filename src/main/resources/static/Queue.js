// Queue.js
// all of this is from chatGPT
class Node {
    constructor(value) {
        this.value = value;
        this.next = null;
    }
}

class Queue {
    constructor() {
        this.front = null;
        this.rear = null;
        this.length = 0;
    }

    // Add an element to the queue (enqueue)
    enqueue(element) {
        const newNode = new Node(element);
        if (this.isEmpty()) {
            this.front = newNode;
            this.rear = newNode;
        } else {
            this.rear.next = newNode;
            this.rear = newNode;
        }
        this.length++;
    }

    // Remove an element from the queue (dequeue)
    dequeue() {
        if (this.isEmpty()) {
            return "Queue is empty";
        }
        const removedValue = this.front.value;
        this.front = this.front.next;
        this.length--;
        if (this.isEmpty()) {
            this.rear = null;
        }
        return removedValue;
    }

    // Check if the queue is empty
    isEmpty() {
        return this.length === 0;
    }

    // View the front element of the queue
    frontElement() {
        if (this.isEmpty()) {
            return "Queue is empty";
        }
        return this.front.value;
    }

    // View all elements in the queue
    printQueue() {
        let current = this.front;
        let result = [];
        while (current) {
            result.push(current.value);
            current = current.next;
        }
        return result.join(" ");
    }

    contains(element) {
        let current = this.front;
        while (current) {
            if (current.value === element) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
}

// Export the Queue class
export default Queue;
