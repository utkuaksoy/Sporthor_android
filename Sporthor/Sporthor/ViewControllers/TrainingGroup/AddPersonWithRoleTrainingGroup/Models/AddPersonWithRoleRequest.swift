//
//  AddPersonWithRoleRequest.swift
//  Sporthor
//
//  Created by GÜRHAN YUVARLAK on 30.10.2025.
//

import Foundation

struct AddPersonWithRoleRequest: Encodable {
    var groupId: String
    var users: [String]
    var coaches: [PersonWithRoleModel]
    var trainingGroupId: String
    
    init(groupId: String = "",
         users: [String] = [],
         coaches: [PersonWithRoleModel] = [],
         trainingGroupId: String = "") {
        self.groupId = groupId
        self.users = users
        self.coaches = coaches
        self.trainingGroupId = trainingGroupId
    }
    
    enum CodingKeys: CodingKey {
        case groupId
        case users
        case coaches
    }
    
    func encode(to encoder: any Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(self.groupId, forKey: .groupId)
        try container.encode(self.users, forKey: .users)
        try container.encode(self.coaches, forKey: .coaches)
    }
}

struct PersonWithRoleModel: Encodable {
    var name: String
    var val: String
    var val2: String
}

struct AddCoachesWithRoleRequest: Encodable {
    var trainingGroupId: String
    var coaches: [PersonWithRoleModel]
    
    init(trainingGroupId: String = "",
         coaches: [PersonWithRoleModel] = []) {
        self.trainingGroupId = trainingGroupId
        self.coaches = coaches
    }
}


