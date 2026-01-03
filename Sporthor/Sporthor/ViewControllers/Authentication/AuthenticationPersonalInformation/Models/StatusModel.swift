//
//  StatusModel.swift
//  Sporthor
//
//  Created by derTurke on 8.02.2025.
//

import UIKit

struct StatusModel {
    var titleText: String
    var titleTextColor: UIColor
    var titleTextFont: UIFont?
    var image: String
    var imageWidth: CGFloat
    var imageHeight: CGFloat
    var descriptionText: String
    var descriptionTextColor: UIColor
    var descriptionTextFont: UIFont?
    
    init(titleText: String = "",
         titleTextColor: UIColor = .clear,
         titleTextFont: UIFont? = .body04Compact,
         image: String = "",
         imageWidth: CGFloat = 18.0,
         imageHeight: CGFloat = 18.0,
         descriptionText: String = "",
         descriptionTextColor: UIColor = DesignKitColorName.contentStrong900.color,
         descriptionTextFont: UIFont? = .body04Compact) {
        self.titleText = titleText
        self.titleTextColor = titleTextColor
        self.titleTextFont = titleTextFont
        self.image = image
        self.imageWidth = imageWidth
        self.imageHeight = imageHeight
        self.descriptionText = descriptionText
        self.descriptionTextColor = descriptionTextColor
        self.descriptionTextFont = descriptionTextFont
    }
}
