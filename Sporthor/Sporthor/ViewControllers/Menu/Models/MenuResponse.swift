//
//  MenuResponse.swift
//  Sporthor
//
//  Created by derTurke on 15.06.2025.
//

import Foundation
import ModelParsers

struct MenuResponse: Decodable {
    @LossyArray var menu: [MenuModel]
}

struct MenuModel: Decodable {
    var menuUserType: MenuUserType?
    @SafeDecode var menuKey: MenuKey
    @SafeDecode var iconPath: String
    @SafeDecode var name: String
    @SafeDecode var url: String
    @LossyArray var subMenus: [MenuModel]
    @SafeDecode var mainMenu: Bool
}

enum MenuKey: Int, Decodable {
    case webRedirect = 0
    case mainMenu
    case updateClub
    case addDocument
    case generateClub
    case trainingGroup
    case sporterClub
    case trainingGroupUsers
    case trainingGroupEdit
    case coachList
}

extension MenuKey: SafeDecodable {
    static func safeDecode(from decoder: Decoder) throws -> MenuKey {
        let container = try decoder.singleValueContainer()
        let rawValue = try? container.decode(Int.self)
        return MenuKey(rawValue: rawValue ?? -1) ?? .webRedirect
    }
}

enum MenuUserType: Int, Decodable {
    case sporther = 0
    case coach
    case manager
}

extension MenuUserType: SafeDecodable {
    static func safeDecode(from decoder: Decoder) throws -> MenuUserType {
        let container = try decoder.singleValueContainer()
        let rawValue = try? container.decode(Int.self)
        return MenuUserType(rawValue: rawValue ?? -1) ?? .sporther
    }
}
