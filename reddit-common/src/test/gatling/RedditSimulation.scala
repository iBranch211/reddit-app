
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RedditSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://www.toreddit.com")

	val acceptJsonHeader = Map("Accept" -> "application/json")

	val loginScenario = exec(http("login")
			.post("/j_spring_security_check")
			.formParam("username", "john")
			.formParam("password", "123"))
	
	val adminScenario = exec(http("list_users")
			.get("/api/users?size=10&page=0&sortDir=asc&sort=username"))
		.pause(1)
		.exec(http("list_roles")
			.get("/api/users/roles"))
		.pause(1)		
		.exec(http("modify_user_roles")
			.put("/api/users/17")
			.headers(acceptJsonHeader)
			.body(StringBody("""{"id":17,"username":"test","enabled":true,"roles":[{"id":5,"name":"ROLE_ADMIN","privileges":[{"id":1,"name":"USER_READ_PRIVILEGE"},{"id":2,"name":"USER_WRITE_PRIVILEGE"}]}],"scheduledPostsCount":0}""")).asJSON
			)
		.pause(1)		
		.exec(http("disable_user")
			.put("/api/users/17")
			.headers(acceptJsonHeader)
			.body(StringBody("""{"id":17,"username":"test","enabled":false,"roles":[{"id":7,"name":"ROLE_USER","privileges":[{"id":3,"name":"POST_LIMITED_PRIVILEGE"}]},{"id":5,"name":"ROLE_ADMIN","privileges":[{"id":1,"name":"USER_READ_PRIVILEGE"},{"id":2,"name":"USER_WRITE_PRIVILEGE"}]}],"scheduledPostsCount":0}""")).asJSON
			)
		.pause(1)		
		.exec(http("enable_user")
			.put("/api/users/17")
			.headers(acceptJsonHeader)
			.body(StringBody("""{"id":17,"username":"test","enabled":true,"roles":[{"id":7,"name":"ROLE_USER","privileges":[{"id":3,"name":"POST_LIMITED_PRIVILEGE"}]},{"id":5,"name":"ROLE_ADMIN","privileges":[{"id":1,"name":"USER_READ_PRIVILEGE"},{"id":2,"name":"USER_WRITE_PRIVILEGE"}]}],"scheduledPostsCount":0}""")).asJSON)



	val profileScenario = exec(http("list_preference")
			.get("/api/user/preference"))
		.pause(1)
		.exec(http("check_available")
			.get("/api/scheduledPosts/available"))
		.pause(1)
		.exec(http("modify_preference")
			.put("/api/user/preference/14")
			.headers(acceptJsonHeader)
			.body(StringBody("""{"id":"14","email":"john@test.com","timezone":"Pacific/Pitcairn","subreddit":"kitten","minScoreRequired":"","timeInterval":"0","noOfAttempts":"0","minTotalVotes":"0"}""")).asJSON)


	val rssScenario = exec(http("list_feeds")
			.get("/api/myFeeds"))
		.pause(1)
		.repeat(2) {
			exec(http("add_feed")
				.post("/api/myFeeds")
				.headers(acceptJsonHeader)
				.body(StringBody("""{"name":"baeldung","url":"http://www.baeldung.com/feed"}""")).asJSON
				.check(jsonPath("""$..id""").find.saveAs("feedId")))
			.exec(http("delete_feed")
				.delete("/api/myFeeds/${feedId}"))
			.pause(1)
		}


	val postScenario = exec(http("list_posts")
		.get("/api/scheduledPosts?size=10&page=0&sortDir=desc&sort=submissionDate"))
	    .repeat(3) {
			exec(http("create_post")
				.post("/api/scheduledPosts")
				.headers(acceptJsonHeader)
				.body(StringBody("""{"title":"test1","url":"https://www.flickr.com/photos/andrewtallon/4950395769","subreddit":"kitten","minScoreRequired":"","timeInterval":"0","noOfAttempts":"0","minTotalVotes":"0","date":"2015-12-30 12:00"}""")).asJSON
			    .check(jsonPath("""$..id""").find.saveAs("postId")))
			.pause(1)
			.exec(http("modify_post")
				.put("/api/scheduledPosts/${postId}")
				.headers(acceptJsonHeader)
				.body(StringBody("""{"id" : ${postId},"title":"test after edit","url":"https://www.flickr.com/photos/andrewtallon/4950395769","subreddit":"kitten","minScoreRequired":"","timeInterval":"0","noOfAttempts":"0","minTotalVotes":"0","date":"2016-11-30 12:00"}""")).asJSON)
			.exec(http("delete_post")
				.delete("/api/scheduledPosts/${postId}"))
		}

	 val adminFlow = scenario("Admin Flow").exec(loginScenario).exec(adminScenario)

	 val userFlow = scenario("User Flow").exec(loginScenario).exec(profileScenario).exec(rssScenario)

	 val userSchedulingFlow = scenario("User scheduling Flow").exec(loginScenario).exec(postScenario)

	setUp(
	  adminFlow.inject(rampUsers(2) over (10 seconds)),
	  userFlow.inject(rampUsers(5) over (5 seconds)),
	  userSchedulingFlow.inject(rampUsers(30) over (2 seconds))
	).protocols(httpProtocol)


}