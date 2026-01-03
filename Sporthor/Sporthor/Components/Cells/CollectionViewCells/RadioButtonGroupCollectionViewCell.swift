//
//  RadioButtonGroupCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 18.02.2025.
//

import UIKit
import ComponentKit

final class RadioButtonGroupCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var radioButtonGroup: CKRadioButtonGroup = {
        let radioButtonGroup = CKRadioButtonGroup()
        radioButtonGroup.translatesAutoresizingMaskIntoConstraints = false
        return radioButtonGroup
    }()
    
    // MARK: - Members
    
    // MARK: - Initialize
    override init(frame: CGRect) {
        super.init(frame: frame)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        contentView.addSubview(radioButtonGroup)
        
        NSLayoutConstraint.activate([
            radioButtonGroup.topAnchor.constraint(equalTo: contentView.topAnchor),
            radioButtonGroup.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            radioButtonGroup.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            radioButtonGroup.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: CKRadioButtonGroupDelegate? = nil,
              options: [String],
              selectedIndex: Int? = nil,
              radioGroupSpacing: CGFloat = 14,
              image: UIImage? = Asset.unchecked.image,
              selectedImage: UIImage? = Asset.checked.image,
              imageLabelSpacing: CGFloat = 4) {
        radioButtonGroup.bind(delegate: delegate,
                              options: options,
                              selectedIndex: selectedIndex,
                              radioGroupSpacing: radioGroupSpacing,
                              image: image,
                              selectedImage: selectedImage,
                              imageLabelSpacing: imageLabelSpacing)
    }
}
