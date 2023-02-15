class Profile{
    #avatarUrl;
    #name;
    #login;
    #bio;
    #website;
    #email;
    #numPublicRepos;
    #numFollowers;
    #numFollowing;
    #createdAt;

    constructor(json) {
        const user = json['data']['user'];
        // These parameters will always exist for any user
        this.#avatarUrl = user['avatarUrl'];
        this.#login = user['login'];
        this.#numPublicRepos = user['repositories']['totalCount'];
        this.#numFollowers = user['followers']['totalCount'];
        this.#numFollowing = user['following']['totalCount'];

        // These parameters may or may not exist
        this.#bio = user['bio'] ? user['bio'] : 'n/a';
        this.#email = user['email'] ? user ['email'] : 'n/a';
        this.#website = user['websiteUrl'] ? user['websiteUrl'] : 'n/a';
        this.#name = user['name'] ? user['name'] : 'n/a';

        // Prettify date
        const date = new Date(user['createdAt'])
        this.#createdAt = date.toDateString();
    }

    get avatarUrl() {
        return this.#avatarUrl;
    }

    get name() {
        return this.#name;
    }

    get login() {
        return this.#login;
    }

    get bio() {
        return this.#bio;
    }

    get website() {
        return this.#website;
    }

    get email() {
        return this.#email;
    }

    get numPublicRepos() {
        return this.#numPublicRepos;
    }

    get numFollowers() {
        return this.#numFollowers;
    }

    get numFollowing() {
        return this.#numFollowing;
    }

    get createdAt() {
        return this.#createdAt;
    }
}

Profile.prototype.toString = function profileToString() {
    return `login: ${this.login}\nname: ${this.name}\nemail: ${this.email}\nwebsite: ${this.website}\ndate joined: ${this.createdAt}\nbio: ${this.bio}\nrepositories: ${this.numPublicRepos}\nfollowers: ${this.numFollowers}\nfollowing: ${this.numFollowing}`;
}

module.exports = {
    Profile: Profile
}