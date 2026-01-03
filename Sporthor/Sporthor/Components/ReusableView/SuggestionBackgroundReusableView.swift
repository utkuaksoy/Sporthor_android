//
//  DecorationBackgroundReusableView.swift
//  Sporthor
//
//  Created by derTurke on 27.03.2025.
//

import UIKit

final class SuggestionBackgroundReusableView: UICollectionReusableView {
    override init(frame: CGRect) {
        super.init(frame: frame)
        self.backgroundColor = DesignKitColorName.backgroundWeak100.color
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
