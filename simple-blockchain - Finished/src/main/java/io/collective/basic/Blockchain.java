package io.collective.basic;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ArrayList;
public class Blockchain {

    List<Block> chain = new ArrayList<Block>();

    public boolean isEmpty() {
        return chain.isEmpty();
    }

    public void add(Block block) {
        chain.add(block);
    }

    public int size() {
        return chain.size();
    }

    public boolean isValid() throws NoSuchAlgorithmException {
        // Check an empty chain
        if (chain.isEmpty()) {
            return true;
        }

        // Check a chain of one
        else if (chain.size() == 1) {
            Block firstBlock = chain.get(0);
            return isMined(firstBlock) && firstBlock.getHash().equals(firstBlock.calculatedHash());
        }

        // Check a chain of many
        else {
            Block currentBlock;
            Block previousBlock;

            // iterate thorugh each block
            for (int i = 1; i < chain.size(); i++) {
                currentBlock = chain.get(i);
                previousBlock = chain.get(i - 1);

                // Check if current block and previous block have been mined
                if (!isMined(currentBlock) || !isMined(previousBlock)) {
                    return false;
                }

                // Check if current block's hash is correct
                if (!currentBlock.getHash().equals(currentBlock.calculatedHash())) {
                    return false;
                }

                // Check if current block's previous hash is correct
                if (!previousBlock.getHash().equals(previousBlock.calculatedHash())) {
                    return false;
                }

                // Check if current block's previous hash matches previous block's hash
                if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                    return false;
                }
            }
        }

        return true;
    }

    /// Supporting functions that you'll need.

    // Mines a block by finding a nonce that satisfies the mining condition
    public static Block mine(Block block) throws NoSuchAlgorithmException {
        Block mined = new Block(block.getPreviousHash(), block.getTimestamp(), block.getNonce());

        while (!isMined(mined)) {
            mined = new Block(mined.getPreviousHash(), mined.getTimestamp(), mined.getNonce() + 1);
        }
        return mined;
    }
    
    // Checks if a block is mined by checking its hash
    public static boolean isMined(Block minedBlock) {
        return minedBlock.getHash().startsWith("00");
    }
}