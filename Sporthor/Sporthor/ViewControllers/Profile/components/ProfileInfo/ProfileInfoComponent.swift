//
//  ProfileInfoCComponent.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//

import ComponentBaseKit
import Foundation

struct ProfileInfoComponent: CollectionComponent {
    typealias ViewModel = ProfileInfoComponentViewModel
    typealias Data = ProfileInfoDataModel
    
    let data: Data
    
    enum CodingKeys: String, CodingKey {
        case data
    }
    
    init(from decoder: any Decoder) throws {
        if let container = try? decoder.container(keyedBy: CodingKeys.self),
           let data: Data = try? container.decode(Data.self, forKey: .data) {
            self.data = data
        } else {
            self.data = try Data(from: decoder)
        }
    }
}
