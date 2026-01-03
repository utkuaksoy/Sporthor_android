//
//  FollowTabCollectionViewCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//

import ComponentBaseKit
import DesignKit
import UIKit

final class FollowTabCollectionViewCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements

    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.font = .bold04Compact
        label.textColor = ColorName.contentStrong900.color
        label.textAlignment = .center
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var countLabel: UILabel = {
        let label = UILabel()
        label.font = .bold04Compact
        label.textColor = ColorName.contentStrong900.color
        label.textAlignment = .center
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var stackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [countLabel, titleLabel])
        stack.axis = .horizontal
        stack.spacing = 4
        stack.alignment = .center
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()
    
    private lazy var indicatorView: UIView = {
        let view = UIView()
        view.backgroundColor = .clear
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    // MARK: - Properties

    override var isSelected: Bool {
        didSet {
            updateAppearance()
        }
    }
    
    // MARK: - Initialization
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Configuration
    func configure(title: String, count: Int) {
        titleLabel.text = title
        if count > .zero {
            countLabel.isHidden = false
            countLabel.text = "\(count)"
        } else {
            countLabel.isHidden = true
        }
        updateAppearance()
    }
    
    private func updateAppearance() {
        indicatorView.isHidden = !isSelected
        indicatorView.backgroundColor = isSelected ? ColorName.contentStrong900.color : .clear
    }
}

// MARK: - Setup

private extension FollowTabCollectionViewCell {
    func setupViews() {
        contentView.addSubview(stackView)
        contentView.addSubview(indicatorView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            stackView.centerXAnchor.constraint(equalTo: contentView.centerXAnchor),
            stackView.centerYAnchor.constraint(equalTo: contentView.centerYAnchor),
            
            indicatorView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            indicatorView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            indicatorView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            indicatorView.heightAnchor.constraint(equalToConstant: 2)
        ])
    }
}
