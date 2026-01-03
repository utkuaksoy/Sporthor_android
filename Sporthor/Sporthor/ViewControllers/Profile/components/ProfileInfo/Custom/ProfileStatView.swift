//
//  ProfileStatView.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 11.02.2025.
//

import UIKit

final class ProfileStatView: UIView {
    
    // MARK: - Private UI Elements
    
    private lazy var countLabel: UILabel = {
        let label = UILabel()
        label.font = .bold04Compact
        label.textColor = DesignKitColorName.contentSub800.color
        label.textAlignment = .center
        return label
    }()
    
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.font = .body04Compact
        label.textColor = DesignKitColorName.contentSub800.color
        label.textAlignment = .center
        return label
    }()
    
    private lazy var stackView: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [countLabel, titleLabel])
        stackView.axis = .horizontal
        stackView.alignment = .center
        stackView.spacing = 2
        return stackView
    }()
    
    // MARK: - Initializers
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupUI() {
        addSubview(stackView)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor)
        ])
    }
    
    // MARK: - Configure Methods
    
    func configure(count: Int, label: String) {
        countLabel.text = "\(count)"
        titleLabel.text = label
    }
}
