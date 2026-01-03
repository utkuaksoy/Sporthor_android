//
//  CareerHistoryContainerCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

final class CareerHistoryContainerCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements

    private let containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(
            textColor: ColorName.contentStrong900.color,
            font: .bold03Compact
        )
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var containerStackView: CKStackView = {
        let stackView = CKStackView(
            axis: .vertical,
            distribution: .fill,
            alignment: .fill,
            spacing: 6
        )
        stackView.addArrangedSubviews([titleLabel, collectionView])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        layout.minimumLineSpacing = .zero
        layout.minimumInteritemSpacing = .zero
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.backgroundColor = .white
        collectionView.register(
            CareerHistoryItemCell.self,
            forCellWithReuseIdentifier: CareerHistoryItemCell.reuseIdentifier
        )
        collectionView.dataSource = self
        collectionView.delegate = self
        collectionView.showsVerticalScrollIndicator = false
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        return collectionView
    }()
    
    // MARK: - Private Properties

    private var items: [CareerHistoryItemModel]?
    
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

    func configure(with model: CareerHistoryDataModel) {
        titleLabel.text = model.title
        items = model.items
        collectionView.reloadData()
    }
}

// MARK: - Setup

private extension CareerHistoryContainerCell {
    func setupViews() {
        contentView.addSubview(containerView)
        containerView.addSubview(containerStackView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerView.topAnchor.constraint(equalTo: contentView.topAnchor),
            containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            containerStackView.topAnchor.constraint(equalTo: containerView.topAnchor, constant: 24),
            containerStackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 16),
            containerStackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -16),
            containerStackView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor)
        ])
    }
}

// MARK: - UICollectionViewDataSource

extension CareerHistoryContainerCell: UICollectionViewDataSource {
    func collectionView(
        _ collectionView: UICollectionView,
        numberOfItemsInSection section: Int
    ) -> Int {
        return items?.count ?? 0
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        cellForItemAt indexPath: IndexPath
    ) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(
            withReuseIdentifier: CareerHistoryItemCell.reuseIdentifier,
            for: indexPath
        ) as? CareerHistoryItemCell,
        let item = items?[indexPath.item] else {
            return UICollectionViewCell()
        }
        
        cell.configure(with: item)
        return cell
    }
}

// MARK: - UICollectionViewDelegateFlowLayout

extension CareerHistoryContainerCell: UICollectionViewDelegateFlowLayout {
    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        sizeForItemAt indexPath: IndexPath
    ) -> CGSize {
        return CGSize(width: collectionView.frame.width, height: 88)
    }
}
