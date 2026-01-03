//
//  FollowContentCollectionViewCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//
import UIKit
import ComponentBaseKit

protocol FollowContentCollectionViewCellDelegate: AnyObject {
    func numberOfRows() -> Int
    func cellForRow(at indexPath: IndexPath) -> FollowerModel?
    func didSelectRow(at indexPath: IndexPath)
    func followButtonTapped(at indexPath: IndexPath)
}

final class FollowContentCollectionViewCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements

    private lazy var collectionView: UICollectionView = {
        let layout = createLayout()
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.backgroundColor = .clear
        collectionView.showsVerticalScrollIndicator = false
        collectionView.register(FollowerCollectionViewCell.self, forCellWithReuseIdentifier: FollowerCollectionViewCell.reuseIdentifier)
        collectionView.contentInset = .init(top: 16, left: .zero, bottom: 16, right: .zero)
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        return collectionView
    }()
    
    // MARK: - Properties
    weak var delegate: FollowContentCollectionViewCellDelegate?
    
    // MARK: - Initialization
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        collectionView.frame = bounds
    }
    
    // MARK: - Setup Methods
    private func setupViews() {
        contentView.addSubview(collectionView)
    }
    
    private func setupConstraints() {
        NSLayoutConstraint.activate([
            collectionView.topAnchor.constraint(equalTo: contentView.topAnchor),
            collectionView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            collectionView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    private func createLayout() -> UICollectionViewLayout {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        layout.minimumLineSpacing = 12
        layout.minimumInteritemSpacing = 12
        layout.itemSize = CGSize(width: UIScreen.main.bounds.width, height: 48)
        return layout
    }
    
    // MARK: - Public Methods
    func reloadData() {
        collectionView.reloadData()
    }
}

// MARK: - UICollectionViewDataSource & UICollectionViewDelegate
extension FollowContentCollectionViewCell: UICollectionViewDataSource, UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return delegate?.numberOfRows() ?? 0
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: FollowerCollectionViewCell.reuseIdentifier, for: indexPath) as? FollowerCollectionViewCell,
              let model = delegate?.cellForRow(at: indexPath) else {
            return collectionView.dequeueEmptyReusableCell(with: indexPath)
        }
        cell.configure(with: model.toCellModel(), delegate: self, at: indexPath)
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        delegate?.didSelectRow(at: indexPath)
    }
} 

extension FollowContentCollectionViewCell: FollowerCellDelegate {
    func followButtonTapped(at indexPath: IndexPath) {
        delegate?.followButtonTapped(at: indexPath)
    }
}
