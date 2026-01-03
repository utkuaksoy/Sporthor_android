//
//  NextMatchesCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 11.02.2025.
//

import ComponentBaseKit
import DesignKit
import UIKit

protocol NextMatchesCellDelegate: AnyObject {
    
}

final class NextMatchesCell: UICollectionViewCell, ReusableView, ComponentDisplayer, ComponentDisplayerViewModelConfigurable {
    
    // MARK: - Private UI Elements
    
    private lazy var containerView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var containerStackView: UIStackView = {
        let stackView = UIStackView(
            arrangedSubviews: [
                titleLabelContainerView,
                collectionView
            ]
        )
        stackView.axis = .vertical
        stackView.alignment = .fill
        stackView.distribution = .fill
        stackView.spacing = 8
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var titleLabelContainerView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.text = "Sıradaki maçlar"
        label.font = .bold03Compact
        label.textColor = DesignKitColorName.contentStrong900.color
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        layout.minimumLineSpacing = 8
        layout.minimumInteritemSpacing = 8
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.backgroundColor = .clear
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        collectionView.contentInset = UIEdgeInsets.init(top: .zero, left: 16, bottom: .zero, right: 16)
        return collectionView
    }()
    
    // MARK: - Private Properties
    
    private var viewModel: NextMatchesComponentViewModel?
    private weak var delegate: NextMatchesCellDelegate?
    
    // MARK: - Initializer
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Configure
    
    func configure(
        with viewModel: NextMatchesComponentViewModel,
        at indexPath: IndexPath,
        delegate: NextMatchesCellDelegate?
    ) {
        self.viewModel = viewModel
        self.delegate = delegate
        
        configureTitleLabel(title: viewModel.title)
        configureCollectionView()
    }
    
    private func configureTitleLabel(title: String?) {
        titleLabel.text = title
    }
    
    private func configureCollectionView() {
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.register(
            NextMatchItemCell.self,
            forCellWithReuseIdentifier: NextMatchItemCell.reuseIdentifier
        )
    }
}

// MARK: - Setup

private extension NextMatchesCell {
    func setupViews() {
        contentView.addSubview(containerView)
        containerView.addSubview(containerStackView)
        titleLabelContainerView.addSubview(titleLabel)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            containerView.topAnchor.constraint(equalTo: contentView.topAnchor),
            containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            containerStackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor),
            containerStackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor),
            containerStackView.topAnchor.constraint(equalTo: containerView.topAnchor),
            containerStackView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor),
            
            titleLabel.leadingAnchor.constraint(equalTo: titleLabelContainerView.leadingAnchor, constant: 16),
            titleLabel.trailingAnchor.constraint(equalTo: titleLabelContainerView.trailingAnchor, constant: -16),
            titleLabel.topAnchor.constraint(equalTo: titleLabelContainerView.topAnchor),
            titleLabel.bottomAnchor.constraint(equalTo: titleLabelContainerView.bottomAnchor),
        ])
    }
}

extension NextMatchesCell: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
    func collectionView(
        _ collectionView: UICollectionView,
        numberOfItemsInSection section: Int
    ) -> Int {
        return viewModel?.numberOfItemsMatches ?? .zero
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        cellForItemAt indexPath: IndexPath
    ) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(
            withReuseIdentifier: NextMatchItemCell.reuseIdentifier,
            for: indexPath
        ) as? NextMatchItemCell else { return UICollectionViewCell() }
        let item = viewModel?.getItem(at: indexPath)
        cell.configure(with: item)
        return cell
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        sizeForItemAt indexPath: IndexPath
    ) -> CGSize {
        return CGSize(width: 260, height: 102)
    }
}
