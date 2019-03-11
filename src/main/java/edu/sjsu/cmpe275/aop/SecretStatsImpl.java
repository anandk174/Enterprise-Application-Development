package edu.sjsu.cmpe275.aop;
import java.util.*;

public class SecretStatsImpl implements SecretStats {
    /***
     * Following is a dummy implementation.
     * You are expected to provide an actual implementation based on the requirements.
     */
	
	HashMap<String, ArrayList<UUID>> sharingOccurance = new HashMap<String, ArrayList<UUID>>();
	HashMap<String, ArrayList<UUID>> sharedOccurance = new HashMap<String, ArrayList<UUID>>();
	HashMap<String, ArrayList<UUID>> createOccurance = new HashMap<String, ArrayList<UUID>>();
	
	HashMap<String, Integer> trustScore = new HashMap<String, Integer>();
	HashMap<UUID, Integer> secretScore = new HashMap<UUID, Integer>();	
	HashMap<UUID, String> secrets = new HashMap<UUID, String>();
	
	//to store share requests
	ArrayList<Object[]> shareCommands = new ArrayList<Object[]>();
	
//	@Override
	public void resetStatsAndSystem() {
//		 TODO Auto-generated method stub
		sharingOccurance.clear();
		sharedOccurance.clear();
		createOccurance.clear();
		trustScore.clear();
		secretScore.clear();
		secrets.clear();
		
	}

	public int getLengthOfLongestSecret() {
		// TODO Auto-generated method stub	
		String longestString = Collections.max(secrets.values());
		return longestString.length();
	}


	public String getMostTrustedUser() {
		// TODO Auto-generated method stub
		int maxTrustScore = Collections.max(trustScore.values());
		String mostTrusted = "";
		for (String trustedUser: trustScore.keySet()) {
//			if(trustScore.get(trustedUser) == maxTrustScore)
//				return trustedUser;
			if(trustScore.get(trustedUser) == maxTrustScore) {
				if(mostTrusted.equals("") || trustedUser.compareTo(mostTrusted) < 0)
					mostTrusted = trustedUser;				
			}
		}
		if(mostTrusted.equals(""))
			return null;
		else
			return mostTrusted;
	}

//	@Override
	public String getWorstSecretKeeper() {
		// TODO Auto-generated method stub
		int minTrustScore = Collections.min(trustScore.values());
		String minTrusted="";
		for (String untrustedUser: trustScore.keySet()) {
//			if(trustScore.get(untrustedUser) == minTrustScore)
//				return untrustedUser;
			
			if(trustScore.get(untrustedUser) == minTrustScore) {
				if(minTrusted.equals("") || untrustedUser.compareTo(minTrusted) < 0)
					minTrusted = untrustedUser;
			}
		}
		if(minTrusted.equals(""))
			return null;
		else
			return minTrusted;
	}

//	@Override
	public String getBestKnownSecret() {
		// TODO Auto-generated method stub
		int maxScore = Collections.max(secretScore.values());
		String mostKnown = null;
		for( UUID secret: secretScore.keySet()) {
			if(secretScore.get(secret) == maxScore)
				return secrets.get(secret);
			if(secretScore.get(secret) == maxScore) {
				if(mostKnown == null || secrets.get(secret).compareTo(mostKnown) < 0)
					mostKnown = secrets.get(secret);
			}
		}
		if(mostKnown == null)
			return null;
		else
			return mostKnown;
	}
	
	/**************************    Access Control Aspect Functions     ***************************/
	
	// function to check if the user is authorized to either read or share secret
	// called in AccessControlAspect.java
	public void authorizeReadShare(String userId, UUID secretId) {		
		
		boolean hasCreated = false;
		boolean hasBeenSharedWith = false;
		
		//a loop to check if the user has created the secret that he/she wants to share
		if(createOccurance.get(userId) != null) {
			for(UUID secretCreated: createOccurance.get(userId)) {
				if(secretCreated == secretId) {
					hasCreated = true;
					break;
				}			
			}
		}
		
		
		//loop to check if the secret has been shared with the user who wants to share it
		if(sharedOccurance.get(userId) != null) {
			for(UUID secretPossesed: sharedOccurance.get(userId)) {
				if(secretPossesed == secretId) {
					hasBeenSharedWith = true;
					break;
				}
			}
		}
		
		
		//if the user has neither created nor shared the secret then we throw not authorized exception
		if(hasCreated != true && hasBeenSharedWith != true)
			throw new NotAuthorizedException();	
	}	
	
	//function to check if the user is authorized to un-share a secret
	public void authorizeUnshare(String userId, UUID secretId) {
		
		boolean hasCreated = false;
		
		//a loop to check if the user has created the secret that he/she wants to share
		if(createOccurance.get(userId).contains(secretId)) {
			hasCreated = true;
		}	
		
		if(hasCreated != true) throw new NotAuthorizedException();
		
	}
	
	/**************************    END     **********************************/
	
	
	/**************************    Stats Aspect Functions     ***************************/
	
	//function called when a valid user wants to share a secret with other user
	public void whenShare(Object[] params) {
		if (shareCommands == null) {
			shareCommands.add(params);			
		}
		else {
			for(Object[] tuple: shareCommands) {
				if( Arrays.equals(params, tuple)) {
					return;
				}
			}
			shareCommands.add(params);						
		}
		
		String userId = params[0].toString();
		UUID secretId = (UUID)params[1];
		String targetUserId = params[2].toString();
		updateSharingOccurance(userId, secretId);
		updateSharedOccurance(targetUserId, secretId);
		
		//when someone shares secret with a user, the trust score of the user increases
		if(trustScore.containsKey(targetUserId)) {
			int incScore = trustScore.get(targetUserId);
			incScore+=1;
			trustScore.replace(targetUserId, incScore);
		}
		else {
			trustScore.put(targetUserId, 1);
		}
		
		//when a user shares a secret with someone, the trust score of the user decreases
		if(trustScore.containsKey(userId)) {
			int decScore = trustScore.get(userId);
			decScore-=1;
			trustScore.replace(userId, decScore);
		}
		else {
			trustScore.put(userId, -1);
		}
		
		//when a secret is shared successfully it's score
		if(secretScore.containsKey(secretId)) {
			int secScore = secretScore.get(secretId);
			secScore++;
			secretScore.replace(secretId, secScore);
		}
		else {
			secretScore.put(secretId,1);
		}
			
	}
	
	//function that is called when creator of a secert decides to unshare a secret with a user
	public void whenUnshare(Object[] params) {
		UUID secretId = (UUID) params[1];
		String targetUserId = params[2].toString();
		if(sharedOccurance.get(targetUserId).contains(secretId)) 
			sharedOccurance.get(targetUserId).remove(secretId);
	}
	
	
	/**************************    END     **********************************/
	
	//function to add the UUID of the secret, to the list of the secrets that a user has shared 
	public void updateSharingOccurance(String userName, UUID uuid) {	
		
		
					
		//if the user is sharing the secret for the first time then we initialize the corresponding ArrayList
		if(sharingOccurance.get(userName) == null) {
			sharingOccurance.put(userName, new ArrayList<UUID>());
		}
		else if(sharingOccurance.get(userName).contains(uuid)) {
			return;
		}
		
		//we add the uuid to the ArrayList of the corresponding user
		sharingOccurance.get(userName).add(uuid);
	}
	
	//function to add the UUID of the secret, to the list of the secrets that have been shared with the user 
	public void updateSharedOccurance(String userName, UUID uuid) {
		
				
		if(sharedOccurance.get(userName) == null) {
			sharedOccurance.put(userName, new ArrayList<UUID>());
		}
		else if(sharedOccurance.get(userName).contains(uuid)){
			return;
		}
		sharedOccurance.get(userName).add(uuid);
	}
	
	//function to update the HashMap containing secret creators as key and the UUIDs of their created secret as values
	public void updateCreateOccurance(String userName, UUID uuid, String secretContent) {
		
		//initialize the ArrayList if a user is creating a secret for the first time 
		if(createOccurance.get(userName) == null) {
			createOccurance.put(userName, new ArrayList<UUID>());
		}
		
		createOccurance.get(userName).add(uuid);
		secrets.put(uuid, secretContent);
		
	}
	
	
    
}



