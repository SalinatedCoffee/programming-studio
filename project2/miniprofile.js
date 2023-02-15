class MiniProfile{
    #avatarUrl;
    #name;
    #login;

    constructor(json) {
        // These parameters will always exist for any user
        this.#avatarUrl = json['avatarUrl'];
        this.#login = json['login'];

        // These parameters may or may not exist
        this.#name = json['name'] ? json['name'] : 'n/a';
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
}

MiniProfile.prototype.toString = function miniProfileToString() {
    return `login: ${this.login}\nname: ${this.name}`;
}

module.exports = {
    MiniProfile: MiniProfile
}