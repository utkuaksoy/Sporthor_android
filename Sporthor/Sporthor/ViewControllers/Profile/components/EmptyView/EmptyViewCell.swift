//
//  EmptyViewCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.03.2025.
//

import ComponentBaseKit
import DesignKit
import UIKit

public protocol EmptyViewCellDelegate: AnyObject {
    
}

final class EmptyViewCell: UICollectionViewCell, ReusableView, ComponentDisplayer, ComponentDisplayerViewModelConfigurable {
    
    // MARK: - Private UI Elements

    private let emptyImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.tintColor = ColorName.backgroundWeak100.color
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private let emptyTitleLabel: UILabel = {
        let label = UILabel()
        label.textColor = ColorName.contentSoft600.color
        label.font = .heading04
        label.textAlignment = .center
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var stackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [emptyImageView, emptyTitleLabel])
        stack.axis = .vertical
        stack.alignment = .center
        stack.spacing = 8
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()
    
    // MARK: - Private Properties
    
    private var viewModel: EmptyViewComponentViewModel?
    private weak var delegate: EmptyViewCellDelegate?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func configure(
        with viewModel: EmptyViewComponentViewModel,
        at indexPath: IndexPath,
        delegate: EmptyViewCellDelegate?
    ) {
        self.viewModel = viewModel
        self.delegate = delegate
        configureTitleLabel()
        configureImageView()
    }
    
    private func configureTitleLabel() {
        emptyTitleLabel.text = viewModel?.title
    }

    private func configureImageView() {
        emptyImageView.image = UIImage(systemName: "lock.circle.fill")
    }
}

private extension EmptyViewCell {
    
    func setupViews() {
        contentView.addSubview(stackView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            stackView.centerXAnchor.constraint(equalTo: contentView.centerXAnchor),
            stackView.centerYAnchor.constraint(equalTo: contentView.centerYAnchor),
            
            emptyImageView.widthAnchor.constraint(equalToConstant: 72),
            emptyImageView.heightAnchor.constraint(equalToConstant: 72)
        ])
    }
}
