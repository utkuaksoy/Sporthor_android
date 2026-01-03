//
//  CreateStoryViewController.swift
//  Sporthor
//
//  Created by derTurke on 26.04.2025.
//
//

import UIKit
import ComponentKit

final class CreateStoryViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: CreateStoryPresenterProtocol {
        get { return self.basePresenter as! CreateStoryPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var gradientView: CKGradientView = {
        let gradientView = CKGradientView(
            colors: [DesignKitColorName.backgroundPrimaryGreen.color.withAlphaComponent(0.2),
                     .clear],
            startPoint: CGPoint(x: 0.0, y: 0.0),
            endPoint: CGPoint(x: 0.5, y: 0.7)
        )
        gradientView.translatesAutoresizingMaskIntoConstraints = false
        return gradientView
    }()
    
    private lazy var cameraImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.image = Asset.cameraWhite.image
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        return imageView
    }()
    
    private lazy var cameraLabel: CKLabel = {
        let label = CKLabel(text: "Kamera", textColor: .white, font: .bold04Compact)
        return label
    }()
    
    private lazy var cameraStackView: CKStackView = {
        let stackView = CKStackView(alignment: .center, spacing: 8)
        stackView.addArrangedSubviews([cameraImageView, cameraLabel])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var cameraView: UIView = {
        let view = UIView()
        view.backgroundColor = .white.withAlphaComponent(0.04)
        view.setCornerRadius(16)
        view.setBorderWidth(1)
        view.setBorderColor(DesignKitColorName.backgroundSurface800.color)
        view.isUserInteractionEnabled = true
        let tapGesture = UITapGestureRecognizer(target: self,
                                                action: #selector(didTappedCameraView(_:)))
        view.addGestureRecognizer(tapGesture)
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var headerLabel: CKLabel = {
        let label = CKLabel(text: "Galeri", textColor: .white, font: .bold03Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.backgroundColor = .clear
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        return collectionView
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        presenter.viewWillAppear()
    }
    
    // MARK: - Custom Methods
    @objc private func didTappedCameraView(_ sender: UITapGestureRecognizer) {
        presenter.didTappedCameraView()
    }
}

// MARK: - CreateStoryPresenterDelegate
extension CreateStoryViewController: CreateStoryPresenterDelegate {
    override func didSetBackgroundColor(_ color: UIColor) {
        view.backgroundColor = DesignKitColorName.contentStrong900.color
    }
    
    func prepareUI() {
        view.addSubview(gradientView)
        gradientView.addSubview(headerLabel)
        cameraView.addSubview(cameraStackView)
        gradientView.addSubview(cameraView)
        view.addSubview(collectionView)
        
        NSLayoutConstraint.activate([
            gradientView.topAnchor.constraint(equalTo: view.topAnchor),
            gradientView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            gradientView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            gradientView.heightAnchor.constraint(equalToConstant: 258),
            
            headerLabel.leadingAnchor.constraint(equalTo: gradientView.leadingAnchor, constant: 16),
            headerLabel.bottomAnchor.constraint(equalTo: gradientView.bottomAnchor, constant: -16),
            
            cameraView.bottomAnchor.constraint(equalTo: headerLabel.topAnchor, constant: -24),
            cameraView.leadingAnchor.constraint(equalTo: gradientView.leadingAnchor, constant: 16),
            cameraView.trailingAnchor.constraint(equalTo: gradientView.trailingAnchor, constant: -16),
            cameraView.heightAnchor.constraint(equalToConstant: 82),
            
            cameraStackView.topAnchor.constraint(equalTo: cameraView.topAnchor, constant: 16),
            cameraStackView.leadingAnchor.constraint(equalTo: cameraView.leadingAnchor),
            cameraStackView.trailingAnchor.constraint(equalTo: cameraView.trailingAnchor),
            cameraStackView.bottomAnchor.constraint(equalTo: cameraView.bottomAnchor, constant: -16),
            
            
            collectionView.topAnchor.constraint(equalTo: gradientView.bottomAnchor),
            collectionView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            collectionView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
    }
    
    func prepareNavigationBar() {
        if let navCon = navigationController as? CustomNavigationController {
            navCon.isCloseWhiteExist = true
            
            let titleAttributes: [NSAttributedString.Key: Any] = [
                .foregroundColor: UIColor.white,
                .font: UIFont.bold03Compact
            ]
            navCon.navigationBar.titleTextAttributes = titleAttributes
        }
    }
    
    func prepareNavigationDelegate() {
        if let navCon = navigationController as? CustomNavigationController {
            navCon.customDelegate = self
        }
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.collectionView.reloadData()
        }
    }
}

// MARK: - UICollectionViewDataSource
extension CreateStoryViewController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return presenter.assetArray.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = SelectableImageViewCollectionViewCell.dequeue(from: collectionView, at: indexPath)
        cell.bind(presenter.assetArray[indexPath.item],
                  multipleSelected: false)
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        presenter.didSelectItem(at: indexPath)
    }
}

// MARK: - UICollectionViewDelegateFlowLayout
extension CreateStoryViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: (collectionView.frame.size.width - 6) / 3,
                      height: (collectionView.frame.size.width - 6) / 3)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 3
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 3
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return .zero
    }
}


// MARK: - CustomNavigationControllerDelegate
extension CreateStoryViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        switch type {
        case .close:
            presenter.didTappedClose()
        default:
            break
        }
    }
}
