//
//  UpdateGroupRequestModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.04.2025.
//

struct UpdateGroupRequestModel: Encodable {
    var groupId: String?
    var name: String?
    var image: String?
    var newUsers: [String]?
    
    init(groupId: String? = nil, name: String? = nil, image: String? = nil, newUsers: [String]? = nil) {
        self.groupId = groupId
        self.name = name
        self.image = image
        self.newUsers = newUsers
    }
}
