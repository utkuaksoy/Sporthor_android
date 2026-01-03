//
//  PersonalInfoItemCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 19.03.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

// MARK: - DetailStackView

final class PersonalInfoItemCell: UIStackView {
    
    // MARK: - Private UI Elements
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(
            textColor: ColorName.contentDisable300.color,
            textAlignment: .center,
            font: .interTight400
        )
        return label
    }()
    
    private lazy var valueLabel: CKLabel = {
        let label = CKLabel(
            textColor: ColorName.backgroundWeak100.color,
            textAlignment: .center,
            font: .bold04Compact
        )
        return label
    }()
    
    private lazy var flagImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.layer.cornerRadius = 9
        imageView.clipsToBounds = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 18).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 18).isActive = true
        return imageView
    }()
    
    private lazy var valueStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .horizontal
        stackView.spacing = 4
        stackView.alignment = .center
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubview(flagImageView)
        stackView.addArrangedSubview(valueLabel)
        return stackView
    }()
    
    // MARK: - Initializer
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupStack()
    }
    
    required init(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupStack() {
        axis = .vertical
        spacing = 4
        alignment = .center
        
        addArrangedSubview(titleLabel)
        addArrangedSubview(valueStackView)
    }
    
    func setTitle(_ title: String) {
        titleLabel.text = title
    }
    
    func setValue(_ value: String) {
        valueLabel.text = value
        flagImageView.isHidden = true
    }
    
    func setValue(icon: String?, text: String?) {
        valueLabel.text = text
        if let iconURL = icon {
            flagImageView.setImage(with: iconURL)
            flagImageView.isHidden = false
        } else {
            flagImageView.isHidden = true
        }
    }
}
