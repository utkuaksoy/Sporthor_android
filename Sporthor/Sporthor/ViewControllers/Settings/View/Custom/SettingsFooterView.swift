//
//  SettingsFooterView.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//

import DesignKit
import UIKit

final class SettingsFooterView: UIView {
    
    // MARK: - Private UI Elements

    private lazy var stackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = 16
        stackView.alignment = .leading
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var logoImageView: UIImageView = {
        let imageView = UIImageView(image: Asset.settingsFooterIcon.image)
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var copyrightLabel: UILabel = {
        let label = UILabel()
        label.text = L10n.Settings.Footer.title
        label.textAlignment = .center
        label.font = .interTight400
        label.textColor = ColorName.contentSoft600.color
        return label
    }()
    
    // MARK: - Initializer

    override init(frame: CGRect) {
        super.init(frame: frame)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}

// MARK: - Setup

private extension SettingsFooterView {
    func setupUI() {
        translatesAutoresizingMaskIntoConstraints = false
        backgroundColor = .clear
        
        addSubview(stackView)
        stackView.addArrangedSubview(logoImageView)
        stackView.addArrangedSubview(copyrightLabel)
        
        NSLayoutConstraint.activate([
            heightAnchor.constraint(equalToConstant: 80),
            
            stackView.centerYAnchor.constraint(equalTo: centerYAnchor),
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 16),

            logoImageView.heightAnchor.constraint(equalToConstant: 32),
            logoImageView.widthAnchor.constraint(equalToConstant: 124)
        ])
    }
}
