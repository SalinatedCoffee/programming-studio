class Repository {
    #nameWithOwner;
    #description;

    constructor(json) {
        this.#nameWithOwner = json['nameWithOwner'];
        this.#description = json['description'] ? json['description'] : 'n/a';
    }

    get nameWithOwner() {
        return this.#nameWithOwner;
    }

    get description() {
        return this.#description;
    }
}

Repository.prototype.toString = function repositoryToString() {
    return `owner/name: ${this.nameWithOwner}\ndescription: ${this.description}`;
}

module.exports = {
    Repository: Repository
}