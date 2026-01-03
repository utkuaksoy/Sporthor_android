//
//  EmptyViewComponent.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.03.2025.
//

import ComponentBaseKit
import Foundation

struct EmptyViewComponent: CollectionComponent {
    typealias ViewModel = EmptyViewComponentViewModel
    typealias Data = EmptyViewDataModel
    
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
