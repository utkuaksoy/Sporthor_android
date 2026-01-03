//
//  HeaderCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 17.02.2025.
//

import UIKit
import ComponentKit

final class HeaderCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var label: CKLabel = {
        let label = CKLabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
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
    
    // MARK: - Custom Methods
    private func prepareUI() {
        contentView.addSubview(label)
        
        NSLayoutConstraint.activate([
            label.topAnchor.constraint(equalTo: contentView.topAnchor),
            label.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            label.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            label.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    func bind(delegate: CKLabelDelegate? = nil,
              text: String = "",
              textColor: UIColor = DesignKitColorName.contentSub800.color,
              backgroundColor: UIColor = .clear,
              numberOfLines: Int = 1,
              textAlignment: NSTextAlignment = .natural,
              lineBreakMode: NSLineBreakMode = .byTruncatingTail,
              font: UIFont? = .bold04Compact,
              isUserInteractionEnabled: Bool = false,
              tag: Int = 0) {
        label.ckDelegate = delegate
        label.text = text
        label.textColor = textColor
        label.backgroundColor = backgroundColor
        label.numberOfLines = numberOfLines
        label.textAlignment = textAlignment
        label.lineBreakMode = lineBreakMode
        label.font = font
        label.isUserInteractionEnabled = isUserInteractionEnabled
        label.tag = tag
    }
}
