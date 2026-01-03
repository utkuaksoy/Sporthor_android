//
//  ExperienceDummyModel.swift
//  Sporthor
//
//  Created by derTurke on 18.02.2025.
//

import Foundation

struct ExperienceDummyModel {
    var image: String
    var title: String
    var isSelected: Bool
    
    init(image: String = "", title: String = "", isSelected: Bool = false) {
        self.image = image
        self.title = title
        self.isSelected = isSelected
    }
}
