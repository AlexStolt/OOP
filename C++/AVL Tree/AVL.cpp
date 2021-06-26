#include "AVL.hpp"

/////////////////////////////////////Node Constractor/////////////////////////////////////

AVL::Node::Node(const string& e, Node *parent, Node *left, Node *right){
   
    setElement(e);
    setParent(parent);
    setLeft(left);
    setRight(right);
    height = 1;
}

/////////////////////////////////////Node Methods/////////////////////////////////////////

AVL::Node *AVL::Node::getParent() const{
    return parent;
}

AVL::Node *AVL::Node::getLeft() const{
    return left;
}

AVL::Node *AVL::Node::getRight() const{
    return right;
}

string AVL::Node::getElement() const{
    return element;
}

int AVL::Node::getHeight() const{
    return height;
}



void AVL::Node::setLeft(Node *node){
    left = node; 
}

void AVL::Node::setRight(Node *node){
    right = node;
}

void AVL::Node::setParent(Node *node){
    parent = node;
}

void AVL::Node::setElement(string e){
    element = e;
}

void AVL::Node::setHeight(int h){
    height = h;
}


bool AVL::Node::isLeft() const{
    if(this->parent == NULL)
        return false;

    if(this->parent->left == this)
        return true;
    else
        return false;
}

bool AVL::Node::isRight() const{
    if(this->parent == NULL)
        return false;

    if(this->parent->right == this)
        return true;
    else
        return false;
}

int AVL::Node::rightChildHeight() const{
    if(this->right == NULL)
        return 0;

    return this->right->getHeight();
}

int AVL::Node::leftChildHeight() const{
    if(this->left == NULL)
        return 0;

    return this->left->getHeight();
}

int AVL::Node::updateHeight() {
    if(this->left == NULL && this->right == NULL){
        return 1;
    }
    else if(this->left != NULL && this->right == NULL){
        return this->leftChildHeight() + 1;
    }
    else if(this->left == NULL && this->right != NULL){ 
        return this->rightChildHeight() + 1;
    }
    else if(this->left != NULL && this->right != NULL){
        if (this->left->getHeight() >= this->right->getHeight()){
            return this->leftChildHeight() + 1;
        }
        else {    
            return this->rightChildHeight() + 1;  
        }
    }

    return 0;
}

bool AVL::Node::isBalanced(){
    int balanced = 0;

    if(this->getLeft() == NULL && this->getRight() == NULL){
        return true;
    }
    else if(this->getLeft() != NULL && this->getRight() == NULL){
        balanced = this->leftChildHeight();
    }
    else if(this->getLeft() == NULL && this->getRight() != NULL){  
        balanced = this->rightChildHeight();
    }
    else if(this->getLeft() != NULL && this->getRight() != NULL){
        balanced = this->rightChildHeight() - this->leftChildHeight();
    }

    if(balanced < -1 || balanced > 1){
        return false;
    }

    return true;
}

////////////////////////////////////Itarator Costractor/////////////////////////////////

AVL::Iterator::Iterator(Node *node){
    currNode = node;

    if(node!= NULL)
        nodeStack.push(node);
}

////////////////////////////////////Itarator Methods/////////////////////////////////////

//Find the next node by preorder
AVL::Iterator& AVL::Iterator::operator++() {
    if(nodeStack.empty() == false){
        currNode = nodeStack.top();

        nodeStack.pop();

        //Go to next node
        if(currNode->getRight() != NULL)
            nodeStack.push(currNode->getRight());
        
        if(currNode->getLeft() != NULL)
            nodeStack.push(currNode->getLeft());
        

        //Take top element
        if(nodeStack.empty() == false)
            currNode = nodeStack.top();
        else if(nodeStack.empty() == true)
            currNode = NULL;
    }
    else 
        currNode = NULL;
    
    return *this;
}

AVL::Iterator AVL::Iterator::operator ++(int a) {
    Iterator previous(this->currNode);

    if(nodeStack.empty() == false){
        //Take top element
        currNode = this->nodeStack.top();

        //Pop top element
        this->nodeStack.pop();

        //Go to next node
        if(currNode->getRight() != NULL){
            this->nodeStack.push(currNode->getRight());
        }
        if(currNode->getLeft() != NULL){
            this->nodeStack.push(currNode->getLeft());
        }
    }
    else 
        currNode = NULL;


    return previous;
}

//Return element from the top iterator
string AVL::Iterator::operator *() {
    if(nodeStack.empty() == true)
        return ("\0");
  
    return (nodeStack.top()->getElement());
}

//Check if left operator is not equal to rigth operator
bool AVL::Iterator::operator != (Iterator iter) {
    if(currNode != iter.currNode)
        return true;

    return false;
}

//Check if left operator is equal to right operator
bool AVL::Iterator::operator == (Iterator iter) {
    if(nodeStack.empty() == true) 
        return true;

    if(currNode != iter.currNode)
        return false;

    return true;
}

/////////////////////////////////////AVL Constractor/////////////////////////////////////

AVL::AVL(){
    root = NULL;
    size = 0;
}

AVL::AVL(const AVL& avl){
    root = NULL;
    size = 0;
  
    for(Iterator iter = avl.begin(); iter != avl.end(); ++iter){
        add(iter.nodeStack.top()->getElement());
    }
}

/////////////////////////////////////AVL Methods/////////////////////////////////////

//Return root iterator
AVL::Iterator AVL::begin() const { 
    return Iterator(root);
}

//Return NULL Iterator
AVL::Iterator AVL::end() const { 
    return Iterator(NULL);
}

///call pre_order
std::ostream& operator <<(std::ostream& out, AVL& tree){
    tree.pre_order(out);

    return out;
}

//Print AVL tree with preorder
void AVL::pre_order(std::ostream& out){

    for(Iterator iter = begin(); iter != end(); ++iter)
        out << *iter << " ";

}

//Find the node that contain the string e
bool AVL::contains(string e){

    for(Iterator iter = begin(); iter != end(); ++iter){
        if(iter.currNode->getElement() == e)
            return true;
    }
    
    return false;
}

bool AVL::add(string e){

    //Check if the string already exist
    if(contains(e))
        return false;

    insertNode(root, e);

    return true;
}

bool AVL::rmv(string e){
    Node *removeNode, *parent, *substituteNode, *t2;

    //Check if the string e exist in the tree
    if(root == NULL || contains(e) == false)
        return false;


    //Find the node that it must be removed
    for(Iterator iter = begin(); iter != end(); ++iter){
        if(iter.currNode->getElement() == e){
            removeNode = iter.currNode;
            break;
        }
    }

    parent = removeNode->getParent();

    //First option, removeNode is a leaf
    if(removeNode->getHeight() == 1){
        if(removeNode == root)
            root = NULL;
        else{ 
            if(removeNode->isLeft())
                parent->setLeft(NULL);
            else    
                parent->setRight(NULL);

            updateNodesHeight(parent);
            updateNodesBalance(parent);
        }
    }
    //Second option, removeNode is not a leaf and doesn' t have right subtree
    else if(removeNode->getRight() == NULL){
        substituteNode = removeNode->getLeft();

        if(removeNode == root){
            root = substituteNode;
            substituteNode->setParent(NULL);
        }
        else{
            if(removeNode->isLeft())
                parent->setLeft(substituteNode);
            else
                parent->setRight(substituteNode);

            substituteNode->setParent(parent);
            updateNodesHeight(parent);
            updateNodesBalance(parent);
        }
    }
    //Third option, removeNode is not a leaf and has right subtree
    else{
        substituteNode = findSubstituteNode(removeNode->getRight());
        
        //Checks if the right child isn t the substituteNode of removeNode
        if(substituteNode->getParent() != removeNode){
            if(substituteNode->getRight() != NULL){
                t2 = substituteNode->getRight();
                substituteNode->getParent()->setLeft(t2);
                t2->setParent(substituteNode->getParent());
            }
            else{
                t2 = substituteNode->getParent();
                t2->setLeft(NULL);
                }
        }
        else{
            t2 = substituteNode;
        }

        //RemoveNode is root
        if(removeNode == root){
            root = substituteNode;
            substituteNode->setParent(NULL);
        }
        else{
            if(removeNode->isLeft())
                parent->setLeft(substituteNode);
            else
                parent->setRight(substituteNode);

            substituteNode->setParent(parent);
        }

        if(substituteNode != removeNode->getRight()){
            substituteNode->setRight(removeNode->getRight());
            removeNode->getRight()->setParent(substituteNode);
        }

        //Left child must be exist
        if(removeNode->getLeft() != NULL){
            substituteNode->setLeft(removeNode->getLeft());
            removeNode->getLeft()->setParent(substituteNode); 
        }

        updateNodesHeight(t2);
        updateNodesBalance(t2);
    }

    size--;
    delete(removeNode);

    return true;
}


//Insert a node into AVL tree
void AVL::insertNode(Node *curr, string e){
        
    //Connect root with the AVL tree   
    if(root == NULL){
        Node *newNode = new Node(e, NULL, NULL, NULL);
        root = newNode;
        size++;
        return;
    }

     //if left or right child doesn t exist, create a node and connect to tree
    if((curr->getLeft() == NULL && curr->getElement() > e) || (curr->getRight() == NULL  && curr->getElement() < e)){
        Node *newNode = new Node(e, curr, NULL, NULL);

        if(curr->getLeft() == NULL && curr->getElement() > e)
            curr->setLeft(newNode);

        else if(curr->getRight() == NULL && curr->getElement() < e)
            curr->setRight(newNode);

        updateNodesHeight(newNode);
        updateNodesBalance(newNode);
        size++;
    }
    else{
        //Find where i can insert the node
        if(e < curr->getElement())
            insertNode(curr->getLeft(), e);
        else
            insertNode(curr->getRight(), e);
    }

}

//Find the correct Child for the rotate
AVL::Node *AVL::childOfNode(Node *parent){

    if(parent->leftChildHeight() < parent->rightChildHeight())
        return parent->getRight(); 

    else if(parent->leftChildHeight() > parent->rightChildHeight())
        return parent->getLeft();

    else{
        if(parent->isLeft())
            return parent->getLeft();
        else
            return parent->getRight(); 
    }
        
}
    
//Rebalance the tree
void AVL::updateNodesBalance(Node* node){
   
   //Checks if the tree needs rotate
    for(Node *curr = node; curr != NULL; curr = curr->getParent()) {
        if(curr->isBalanced() == false){
             Node *child = childOfNode(curr);
             Node *grandChild = childOfNode(child);

            //First option one right rotate
            if(child->isLeft() == true && grandChild->isLeft() == true){
                if(curr != root){
                    if(curr->isLeft())
                        curr->getParent()->setLeft(rightRotate(curr));
                    else
                        curr->getParent()->setRight(rightRotate(curr));
                }
                else{
                    root = rightRotate(curr);
                }
            }
            //Second option one left rotate
            else if(child->isRight() == true && grandChild->isRight() == true){
               if(curr != root){
                    if(curr->isLeft())
                        curr->getParent()->setLeft(leftRotate(curr));
                    else
                        curr->getParent()->setRight(leftRotate(curr));
               }
               else{
                   root = leftRotate(curr);
               }
            }

            //Third option one left rotate and then one right rotate
            else if(grandChild->isRight() == true){
                
                curr->setLeft(leftRotate(child));

                if(curr != root){
                    if(curr->isLeft())
                        curr->getParent()->setLeft(rightRotate(curr));
                    else
                        curr->getParent()->setRight(rightRotate(curr));
                }
                else{
                    root = rightRotate(curr);
                }
            }

            //Fourth option one right rotate and then one left rotate
            else if(grandChild->isLeft() == true){
                curr->setRight(rightRotate(child));

                if(curr != root){
                    if(curr->isLeft())
                        curr->getParent()->setLeft(leftRotate(curr));
                    else
                        curr->getParent()->setRight(leftRotate(curr));
                }
                else{
                    root = leftRotate(curr);
                }
            }   

            updateNodesHeight(curr);        
        }
    }
}

//Update the height of the parents of the node
void AVL::updateNodesHeight(Node* node){

    for(Node *curr = node; curr != NULL; curr = curr->getParent()) 
        curr->setHeight(curr->updateHeight()); 
    
}

//Right rotate
AVL::Node *AVL::rightRotate(Node *unBalancedNode){
    Node *newRoot = unBalancedNode->getLeft();
    Node *t2 = newRoot->getRight();
 
    //Rotation
    if(unBalancedNode == root)
        root = newRoot;

    newRoot->setParent(unBalancedNode->getParent());
    newRoot->setRight(unBalancedNode);
    unBalancedNode->setParent(newRoot);
    unBalancedNode->setLeft(t2);
    if(t2 != NULL)
        t2->setParent(unBalancedNode);

    // Update heights
    unBalancedNode->setHeight(unBalancedNode->updateHeight());
    newRoot->setHeight(newRoot->updateHeight());
    

    // Return new root
    return newRoot;
}

//Left Rotate
AVL::Node *AVL::leftRotate(Node *unBalancedNode){
    Node *newRoot = unBalancedNode->getRight();
    Node *t2 = newRoot->getLeft();
 
    //Rotation
    if(unBalancedNode == root)
        root = newRoot;

    newRoot->setParent(unBalancedNode->getParent());
    newRoot->setLeft(unBalancedNode);
    unBalancedNode->setParent(newRoot);
    unBalancedNode->setRight(t2);
    if(t2 != NULL)
        t2->setParent(unBalancedNode);

 
    // Update heights
    unBalancedNode->setHeight(unBalancedNode->updateHeight());
    newRoot->setHeight(newRoot->updateHeight());
    

    // Return new root
    return newRoot;
}

//Find the substitute node for the remove
AVL::Node *AVL::findSubstituteNode(Node *node){

    if(node->getLeft() == NULL)
        return node;

    return findSubstituteNode(node->getLeft());
 }

//Preorder the AVL tree for the dotFile
 void AVL::graphTraversalPreorder(Node *currNode, string& line){
    line.append("    "  + currNode->getElement() + " [shape=square, color=red]" + "\n");

    if(currNode->getLeft() != NULL){
        line.append("    " + currNode->getElement() + " -- " + currNode->getLeft()->getElement() + "\n");
       
        graphTraversalPreorder(currNode->getLeft(), line);
    }

    if(currNode->getRight() != NULL){
        line.append("    " + currNode->getElement() + " -- " + currNode->getRight()->getElement() + "\n");
       
        graphTraversalPreorder(currNode->getRight(), line);
    }
}


//Print AVL tree to dot file
void AVL::print2DotFile(char *filename){
    ofstream dotFile;
    string line;

    dotFile.open(filename);

    line.append("graph AVLTREE {\n");

    graphTraversalPreorder(root, line);


    line.append("}");

    dotFile << line;

    dotFile.close();
}

//Delete all the nodes of the tree
void AVL::deleteTree(Node *currNode){

    if(currNode == NULL)
        return;

    deleteTree(currNode->getLeft());

    deleteTree(currNode->getRight());

    delete(currNode);
}


/////////////////////////////////Operators/////////////////////////////////

AVL& AVL::operator  =(const AVL& avl){
    deleteTree(root);

    root = NULL;
    size = 0;

    for(Iterator iter =avl.begin(); iter != avl.end(); ++iter)
        add(iter.nodeStack.top()->getElement());


    return *this;
}

AVL  AVL::operator  +(const AVL& avl){
    AVL avlTree(*this);

    for(Iterator rightSubtree = avl.begin(); rightSubtree != avl.end(); ++rightSubtree)
        avlTree.add(rightSubtree.nodeStack.top()->getElement());
  

    return avlTree;
}


AVL& AVL::operator +=(const AVL& avl){

    for(Iterator rightSubtree = avl.begin(); rightSubtree != avl.end(); ++rightSubtree)
        add(rightSubtree.nodeStack.top()->getElement());

    return *this;
}

AVL& AVL::operator +=(const string& e){
    add(e);

    return *this;
}

AVL& AVL::operator -=(const string& e){
    rmv(e);
    
    return *this;
}

AVL  AVL::operator  +(const string& e){
    AVL avlTree(*this);

    avlTree += e;

    return avlTree;
}

AVL  AVL::operator  -(const string& e){
    AVL avlTree(*this);

    avlTree -= e;

    return avlTree;
}