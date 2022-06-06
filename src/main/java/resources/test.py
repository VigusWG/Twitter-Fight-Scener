import requests
import os
import json

# To set your enviornment variables in your terminal run the following line:
# export 'BEARER_TOKEN'='<your_bearer_token>'
bearer_token = "AAAAAAAAAAAAAAAAAAAAAHSscgEAAAAAh3%2BytHnZPoWllhbW9ppdaGwRtwE%3DegvK9miICpV1JTw86K0spWRSrLedGeYAFJ3pnkLJ0wYRN8i2P5"

def bearer_oauth(r):
    """
    Method required by bearer token authentication.
    """

    r.headers["Authorization"] = f"Bearer {bearer_token}"
    r.headers["User-Agent"] = "v2FilteredStreamPython"
    return r


def get_rules():
    response = requests.get(
        "https://api.twitter.com/2/tweets/search/stream/rules", auth=bearer_oauth
    )
    if response.status_code != 200:
        raise Exception(
            "Cannot get rules (HTTP {}): {}".format(response.status_code, response.text)
        )
    print(json.dumps(response.json()))
    return response.json()


def delete_all_rules(rules):
    if rules is None or "data" not in rules:
        return None

    ids = list(map(lambda rule: rule["id"], rules["data"]))
    payload = {"delete": {"ids": ids}}
    response = requests.post(
        "https://api.twitter.com/2/tweets/search/stream/rules",
        auth=bearer_oauth,
        json=payload
    )
    if response.status_code != 200:
        raise Exception(
            "Cannot delete rules (HTTP {}): {}".format(
                response.status_code, response.text
            )
        )
    print(json.dumps(response.json()))


def set_rules(delete):
    # You can adjust the rules if needed
    sample_rules = [
        {"value": "is:reply @BigVigus"}
    ]
    payload = {"add": sample_rules}
    response = requests.post(
        "https://api.twitter.com/2/tweets/search/stream/rules",
        auth=bearer_oauth,
        json=payload,
    )
    if response.status_code != 201:
        raise Exception(
            "Cannot add rules (HTTP {}): {}".format(response.status_code, response.text)
        )
    print(json.dumps(response.json()))

'''
https://api.twitter.com/2/tweets/search/stream?expansions=referenced_tweets.id&tweet.fields=id,created_at,text,public_metrics&user.fields=id,name,username
get referenced tweet

Tweet

get reply tweet.....
'''
def get_stream(set):
    #response = requests.get(
    #    "https://api.twitter.com/2/tweets/search/stream?&tweet.fields=conversation_id,referenced_tweets,public_metrics&expansions=.id&user.fields=name,username", auth=bearer_oauth, stream=True,
    #)

    response = requests.get(
        "https://api.twitter.com/2/tweets/search/stream?tweet.fields=referenced_tweets", auth=bearer_oauth, stream=True,
    )

    print(response.status_code)
    if response.status_code != 200:
        raise Exception(
            "Cannot get stream (HTTP {}): {}".format(
                response.status_code, response.text
            )
        )
    for response_line in response.iter_lines():
        print(".")
        if response_line:
            
            json_response = json.loads(response_line)
            #print(json.dumps(json_response, indent=4, sort_keys=True))
            print(json_response)

            #print('\n\n\n')
            nextId = json_response["data"]["referenced_tweets"][0]["id"]
            while 1:
                response2 = requests.get(
                    f"https://api.twitter.com/2/tweets/{nextId}?expansions=author_id,entities.mentions.username&tweet.fields=id,created_at,text,referenced_tweets,public_metrics&user.fields=id,name,username".format(nextId), auth=bearer_oauth, stream=True,
                )
                json2 = response2.json()

                print('\n\n')
                print(json2)
                users = 0
                for i in json2["includes"]["users"][1:]:
                    users += 2 + len(i["username"])

                text = json2["data"]["text"][users:]
                user = json2["includes"]["users"][0]["name"]
                print(f"{text} ~ From: {user}")
                
                if "referenced_tweets" not in json2["data"]:
                    break
                
                nextId = json2["data"]["referenced_tweets"][0]["id"]
                #print('\n')

   

def main():
    rules = get_rules()
    delete = delete_all_rules(rules)
    set = set_rules(delete)
    get_stream(set)


if __name__ == "__main__":
    print("Begin")
    main()