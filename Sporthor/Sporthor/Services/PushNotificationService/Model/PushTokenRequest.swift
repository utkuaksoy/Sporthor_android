//
//  PushTokenRequest.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 27.04.2025.
//

struct PushTokenRequest: Encodable {
    var firebaseToken: String
   
    init(firebaseToken: String) {
        self.firebaseToken = firebaseToken
    }
}
