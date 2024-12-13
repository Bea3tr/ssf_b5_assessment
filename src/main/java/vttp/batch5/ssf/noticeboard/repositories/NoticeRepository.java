package vttp.batch5.ssf.noticeboard.repositories;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Repository
public class NoticeRepository {

	@Autowired @Qualifier("notice")
	private RedisTemplate<String, Object> redisTemplate;

	// TODO: Task 4
	// You can change the signature of this method by adding any number of parameters
	// and return any type
	// 
	/*
	 * Write the redis-cli command that you use in this method in the comment. 
	 * For example if this method deletes a field from a hash, then write the following
	 * redis-cli command 
	 * 	hdel myhashmap a_key
	 *
	 *
	 */
	// set <postId> <jsonString>
	public void insertNotices(String payload) {
		JsonReader reader = Json.createReader(new StringReader(payload));
		JsonObject obj = reader.readObject();
		redisTemplate.opsForValue().set(obj.getString("id"), obj.toString());
	}

	// randomkey
	public void checkHealth() throws Exception {
		redisTemplate.randomKey();
	}

}
