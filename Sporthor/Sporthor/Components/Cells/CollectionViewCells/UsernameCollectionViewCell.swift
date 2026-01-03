//
//  UsernameCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke.
//

import UIKit
import ComponentKit

final class UsernameCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var containerView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        view.backgroundColor = DesignKitColorName.backgroundWeak100.color
        view.setCornerRadius(17)
        return view
    }()
    
    private lazy var label: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color,
                            font: .body04Compact)
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
        containerView.addSubview(label)
        contentView.addSubview(containerView)
        
        NSLayoutConstraint.activate([
            containerView.topAnchor.constraint(equalTo: contentView.topAnchor),
            containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            label.topAnchor.constraint(equalTo: containerView.topAnchor, constant: 8),
            label.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 12),
            label.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -12),
            label.bottomAnchor.constraint(equalTo: containerView.bottomAnchor, constant: -8)
        ])
    }
    
    func bind(text: String? = nil) {
        guard let text = text else { return }
        label.text = text
    }
    
    func bind(text: String, isSelected: Bool) {
        label.text = text
        label.textColor = isSelected ? DesignKitColorName.contentStrong900.color : DesignKitColorName.contentSoft600.color
        containerView.backgroundColor = isSelected ? DesignKitColorName.backgroundPrimaryGreen.color : DesignKitColorName.backgroundWeak100.color
    }
}
